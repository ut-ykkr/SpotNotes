package org.menhera.spotnotes.ui.activity_register;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.menhera.spotnotes.LocationClient;
import org.menhera.spotnotes.R;
import org.menhera.spotnotes.data.Reminder;
import org.menhera.spotnotes.ui.ReminderItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class RegisterMapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    Marker centerMarker;
    RegisterActivity activity;
    boolean inOut = true; // in: true

    public RegisterMapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (RegisterActivity) getActivity();

        final Spinner regLocationPrecision = (Spinner) getView().findViewById(R.id.regLocationPrecision);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.distances_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        regLocationPrecision.setAdapter(adapter);

        final Spinner regLocationInOut = getView().findViewById(R.id.regLocationInOut);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.in_out_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regLocationInOut.setAdapter(adapter2);

        final int[] distances = getResources().getIntArray(R.array.distances_values);
        if (0 == activity.getViewModel().getRadius()) {
            activity.getViewModel().setRadius(distances[0]);
        }
        regLocationPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)

                activity.getViewModel().setRadius(distances[pos]);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        regLocationInOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                if (pos == 0) {
                    activity.getViewModel().setInOut(Reminder.InOut.IN);
                } else {
                    activity.getViewModel().setInOut(Reminder.InOut.OUT);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.regMap);
        mapFragment.getMapAsync(this);

        activity.getViewModel().getReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                Reminder reminder = reminders.get(0);

                int radiusIndex = Arrays.stream(distances).boxed().collect(Collectors.toList()).indexOf(reminder.radius);
                if (0 > radiusIndex) {
                    radiusIndex = 0;
                }
                regLocationPrecision.setSelection(radiusIndex);
                if (reminder.inOut == Reminder.InOut.IN) {
                    regLocationInOut.setSelection(0);
                } else {
                    regLocationInOut.setSelection(1);
                }
                //activity.getViewModel().getReminders().removeObserver(this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_maps, container, false);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 12.0f;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);

        LatLng latLng;
        final Reminder reminder = activity.getViewModel().getReminder();
        if (reminder.hasValidLocation()) {
            latLng = new LatLng(reminder.latitude, reminder.longitude);
        } else {
            latLng = new LatLng(35, 139);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        centerMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng));

        if (!reminder.hasValidLocation()) {
            LocationClient locationClient = new LocationClient(getContext(), new LocationClient.Listener() {
                @Override
                public void onLocationFetched(Location location) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    activity.getViewModel().setLocation(loc.latitude, loc.longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    centerMarker.setPosition(loc);
                }
            });
        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng loc = mMap.getCameraPosition().target;
                activity.getViewModel().setLocation(loc.latitude, loc.longitude);
                centerMarker.setPosition(loc);
            }
        });

    }

}
