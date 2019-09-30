package org.menhera.spotnotes.ui;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class RecordActivity extends AppCompatActivity {
    final public static String ARG_NAME = "name";

    private String name;

    EditText recName;
    SupportMapFragment recMap;
    GoogleMap mMap;
    Marker centerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            name = extras.getString(ARG_NAME);
        }

        Log.d(RecordActivity.class.getName(), "name: " + name);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.recToolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recName = findViewById(R.id.recName);
        if (null!= name) {
            recName.setText(name);
        }

        recMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.recMap);
        recMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setAllGesturesEnabled(false);
                uiSettings.setZoomControlsEnabled(false);

                float initZoom = 14.0f;
                LatLng initLatLng = new LatLng( 35, 139 );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initLatLng, initZoom));

                LocationClient locationClient = new LocationClient(RecordActivity.this, new LocationClient.Listener() {
                    @Override
                    public void onLocationFetched(Location location) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        centerMarker = mMap.addMarker( new MarkerOptions()
                                .title( "" )
                                .position( latLng ) );
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.record_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recActionOK:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
