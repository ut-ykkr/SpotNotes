package org.menhera.spotnotes.ui.activity_record;

import android.location.Address;
import android.location.Geocoder;
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
import androidx.lifecycle.ViewModelProviders;

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
import org.menhera.spotnotes.SpotNotesRepository;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {
    final public static String ARG_NAME = "name";

    private RecordViewModel viewModel;
    private String name;

    EditText recName;
    SupportMapFragment recMap;
    GoogleMap mMap;
    Marker centerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        viewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        if (viewModel.repository == null) {
            viewModel.repository = SpotNotesRepository.getInstance(this);
        }

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

                        viewModel.setLocation(location.getLatitude(), location.getLongitude());
                        viewModel.setAddress (getLocationName(location.getLatitude(), location.getLongitude()));
                    }
                });
            }
        });
    }

    public String getLocationName(double lati, double longi){
        //Address address1 = new Address(this, );
        StringBuffer strAddr = new StringBuffer();
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> address = coder.getFromLocation(lati, longi, 1);
            for (Address addr : address) {
                int idx = addr.getMaxAddressLineIndex();
                for (int i = 0; i <= idx; i++) {
                    strAddr.append(addr.getAddressLine(i));
                }
            }
            //String address = address1.getAdminarea() + address1.getLocality();
            return strAddr.toString();
            //return address
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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
                viewModel.setTitle(recName.getText().toString());
                viewModel.insertRecord();
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
