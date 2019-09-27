package org.menhera.spotnotes;


import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterMapsFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mMap;
    Marker centerMarker;
    RegisterActivity activity;
    boolean inOut = true; // in: true

    public RegisterMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterMapsFragment newInstance(String param1, String param2) {
        RegisterMapsFragment fragment = new RegisterMapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (RegisterActivity) getActivity();

        Spinner regLocationPrecision = (Spinner) getView().findViewById(R.id.regLocationPrecision);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.distances_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        regLocationPrecision.setAdapter(adapter);

        Spinner regLocationInOut = getView().findViewById(R.id.regLocationInOut);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.in_out_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regLocationInOut.setAdapter(adapter2);

        activity.setDistance(RegisterActivity.DISTANCES[0]);// TODO
        regLocationPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                activity.setDistance(RegisterActivity.DISTANCES[pos]);

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
                    activity.getReminderItem().setInOut (ReminderItem.IN);
                } else {
                    activity.getReminderItem().setInOut (ReminderItem.OUT);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.regMap);
        mapFragment.getMapAsync(this);
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

        LatLng latLng = new LatLng( 35, 139 );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        latLng = mMap.getCameraPosition().target;
        centerMarker = mMap.addMarker( new MarkerOptions()
                .title( "+" )
                .position( latLng ) );

        LocationClient locationClient = new LocationClient(getContext(), new LocationClient.Listener() {
            @Override
            public void onLocationFetched(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng loc = mMap.getCameraPosition().target;
                activity.getReminderItem().setLatLon(loc.latitude, loc.longitude);
                centerMarker.setPosition(loc);
            }
        });

        //LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("now Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
    }

}
