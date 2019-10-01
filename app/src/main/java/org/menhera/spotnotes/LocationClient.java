package org.menhera.spotnotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.EventListener;
import java.util.List;

public class LocationClient implements LocationListener {
    final static String TAG = "LocationClient";
    final public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private Context context;
    private boolean gmsAvailable = false;
    private Listener listener;
    private Location lastKnownLocation;
    private boolean updateNeeded;

    // GMS Location
    private FusedLocationProviderClient fusedLocationClient;

    // Android Location
    private LocationManager mLocationManager;
    private String bestProvider;

    public interface Listener extends EventListener {
        public void onLocationFetched (Location location);
    }

    public LocationClient (Context context, Listener listener) {
        this(context, listener, false);
    }

    public LocationClient (Context context, Listener listener, boolean updateNeeded) {
        this.context = context;
        this.listener = listener;
        this.updateNeeded = updateNeeded;

        if (!hasLocationPermission()) {
            showToast(context.getResources().getString(R.string.msg_location_denied));
            return;
        }

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        gmsAvailable = googleApiAvailability.isGooglePlayServicesAvailable (context) == ConnectionResult.SUCCESS;
        if (!gmsAvailable) {
            initializeAndroidLocation ();
        } else {
            if (updateNeeded) {
                initializeGmsNewLocation();
            } else {
                initializeGmsLocation();
            }
        }
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    private void initializeGmsLocation () {
        Log.d(TAG, "Using GMS location");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.w(TAG, "GMS location is null");
                            initializeGmsNewLocation ();
                            return;
                        }

                        callListener(location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "GMS location failed");
                        initializeGmsNewLocation();
                    }
                });

    }

    /**
     * This is needed when Play Services do not have the latest location.
     */
    private void initializeGmsNewLocation () {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.requestLocationUpdates(LocationRequest.create(). setInterval(0), new LocationCallback(){
            @Override
            public void onLocationResult (LocationResult result) {
                if (result == null) {
                    return;
                }
                for (Location location : result.getLocations()) {
                    if (location == null){
                        continue;
                    }
                    fusedLocationClient.removeLocationUpdates(this);
                    callListener(location);
                    break;
                }
            }
        }, Looper.myLooper());
    }

    private void initializeAndroidLocation () {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            if (!updateNeeded) {
                List<String> providers = mLocationManager.getProviders(true);
                for (String provider : providers) {
                    Log.d(TAG, "provider: " + provider);
                    Location l = mLocationManager.getLastKnownLocation(provider);
                    if (null != l) {
                        Log.d(TAG, "Using last known location");
                        callListener(l);
                        return;
                    }
                }
            }

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.NO_REQUIREMENT);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setSpeedRequired(false);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            bestProvider = mLocationManager.getBestProvider(criteria, true);
            if (null == bestProvider) {
                showToast(context.getResources().getString(R.string.msg_no_location_provider));
                return;
            }

            Log.d(TAG, "Using Android framework");
            mLocationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
        } catch (SecurityException ex) {
            showToast(context.getResources().getString(R.string.msg_no_location_provider));
        }
    }

    private void callListener (Location location) {
        if (null == listener) {
            Log.w(TAG, "Listener is null");
            return;
        }
        listener.onLocationFetched(location);
        listener = null;
    }

    @Override
    public void onLocationChanged (Location location) {
        mLocationManager.removeUpdates(this);
        callListener(location);
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "Location provider disabled");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged() called");
    }

    public boolean hasLocationPermission () {
        for (String permission: PERMISSIONS_LOCATION) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected void showToast (String msg) {
        Log.d(TAG, "Toast: " + msg);
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.w (TAG, "" + ex);
        }
    }
}
