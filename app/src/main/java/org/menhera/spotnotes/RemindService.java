package org.menhera.spotnotes;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RemindService extends Service implements LocationListener {
    final static String TAG = "RemindService";
    private LocationManager mLocationManager;
    private String bestProvider;
    public String title;
    public double lat;
    public double lon;
    public String addr;
    public int radius;
    public boolean in;
    public long time;
    public int repeat;
    public String memo;

    final String CHANNEL_ID = "default";
    int notificationCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        JSONObject jsonObject = new JSONObject();

        try {
            title = jsonObject.getString("title" );
            lat = jsonObject.getDouble("lat");
            lon = jsonObject.getDouble("lon" );
            addr = jsonObject.getString("addr");
            radius = jsonObject.getInt("radius");
            in = jsonObject.getBoolean("in");
            time  =jsonObject.getInt("time");
            repeat = jsonObject.getInt("repeat" );
            memo = jsonObject.getString("memo");
        } catch (JSONException e) {
            e.printStackTrace();
        }





        initLocationManager();
        locationStart();

        return START_NOT_STICKY;
    }

    private void initLocationManager() {
        // インスタンス生成
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 詳細設定
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        bestProvider = mLocationManager.getBestProvider(criteria, true);
    }


    private void locationStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mLocationManager.requestLocationUpdates(bestProvider, 60000, 3, this);
    }

    private void locationStop() {
        mLocationManager.removeUpdates(this);
    }



    public void showNotification (String title, String description){




        this.createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.btn_dialog)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationCount++;
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationCount, builder.build());
    }


}

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "通知";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());
        Location dist = new Location("gps");
        dist.setLongitude(lon);
        dist.setLatitude(lat);

        float distance = location.distanceTo(dist);
        Log.d(TAG, "dist : " + distance);
        locationStop();





        if((in && distance <= radius ) || (!in && distance > radius)){

            showNotification(title, memo);


        }





        stopSelf();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
