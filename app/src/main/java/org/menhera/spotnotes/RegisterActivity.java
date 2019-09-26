package org.menhera.spotnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import org.menhera.spotnotes.ui.ListPagerAdapter;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    int year;
    int month;
    int day;
    int hour;
    int minute;

    Calendar cldr;
    EditText regName;
    String notes = "";


    final static int[] DISTANCES = {50, 100, 200, 500, 1000, 5000, 10000};

    ReminderItem reminderItem;
    private GoogleMap mMap;
    private ScrollView mScrollView;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setDate (int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        long milliseconds = getTimeInMillis();
        reminderItem.setMilliseconds(milliseconds);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setTime (int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        long milliseconds = getTimeInMillis();
        reminderItem.setMilliseconds(milliseconds);
    }

    public void setDistance (int distance) {
        reminderItem.setDistance(distance);
    }

    public ReminderItem getReminderItem() {
        return reminderItem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // @todo move things into fragments and support classes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.regToolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListPagerAdapter pagerAdapter = new ListPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(getString(R.string.tab_location), new RegisterMapsFragment());
        pagerAdapter.addFragment(getString(R.string.tab_details), new RegisterMainFragment());
        ViewPager viewPager = findViewById(R.id.regViewPager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.regTabLayout);
        tabLayout.setupWithViewPager(viewPager);


        cldr = Calendar.getInstance();

        reminderItem = new ReminderItem("", "");
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minute = cldr.get(Calendar.MINUTE);

        Resources res = getResources();
        reminderItem.repeatLabels = res.getStringArray(R.array.repeat_array);
        reminderItem.inOutLabels = res.getStringArray(R.array.in_out_array);
        reminderItem.dayNames = res.getStringArray(R.array.days);


        regName = findViewById(R.id.regName);
    }

    public ReminderItem buildReminderItem () {
        reminderItem.setTitle (regName.getText().toString());
        reminderItem.setNotes(notes);
        double lat = reminderItem.getLat();
        double lon = reminderItem.getLon();
        reminderItem.setLocationName(getLocationName(lat, lon));
        return reminderItem;
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
        inflater.inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.regActionOK:
                // Complete action
                SpotNotesApplication app = (SpotNotesApplication) getApplication();
                buildReminderItem();
                String title = reminderItem.getTitle();
                if (TextUtils.isEmpty(title)) {
                    regName.setError(getResources().getString(R.string.required));
                    return false;
                }
                int index = app.addReminderItem(reminderItem);
                // AlarmManager (index)
                Context context = getBaseContext();
                Intent intent = new Intent(context, RemindService.class);
                intent.putExtra("index", index);
                PendingIntent pendingIntent
                        = PendingIntent.getService(
                        context, -1, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager
                        = (AlarmManager)
                        context.getSystemService(ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderItem.getMilliseconds(), pendingIntent);
                finish ();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public long getTimeInMillis () {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        return cal.getTimeInMillis();
    }

    public int getDistance () {
        return reminderItem.getDistance();
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

        LocationClient locationClient = new LocationClient(this, new LocationClient.Listener() {
            @Override
            public void onLocationFetched(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        });

        //LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("now Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
    }


}
