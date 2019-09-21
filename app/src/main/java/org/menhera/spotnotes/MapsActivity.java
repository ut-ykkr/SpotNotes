package org.menhera.spotnotes;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 12.0f;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng latLng = new LatLng( 35, 139 );
//        mMap.addMarker( new MarkerOptions()
//                .title( "ピンのタイトル" )
//                .position( latLng ) );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("now Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
    }


}
