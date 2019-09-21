package org.menhera.spotnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int distance; // meters
    boolean inOut; // in: true

    DatePickerDialog picker;
    TimePickerDialog timePicker;
    Button regDateButton;
    Button regTimeButton;
    Calendar cldr;
    EditText regName;
    EditText regNotes;

    final int[] DISTANCES = {50, 100, 200, 500, 1000, 5000, 10000};

    ReminderItem reminderItem;
    private GoogleMap mMap;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Reminder");

        cldr = Calendar.getInstance();

        reminderItem = new ReminderItem("", "");
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minute = cldr.get(Calendar.MINUTE);

        regDateButton = findViewById(R.id.regDateButton);
        regDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int monthOfYear, int dayOfMonth) {
                                day = dayOfMonth;
                                month = monthOfYear;
                                year = year2;
                                long milliseconds = getTimeInMillis ();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String text = sdf.format(new Date(milliseconds));
                                regDateButton.setText(text);
                                reminderItem.setMilliseconds(milliseconds);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        regTimeButton = findViewById(R.id.regTimeButton);
        regTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker = new TimePickerDialog(RegisterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                hour = sHour;
                                minute = sMinute;
                                long milliseconds = getTimeInMillis ();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                                String text = sdf.format(new Date(milliseconds));
                                regTimeButton.setText(text);
                                reminderItem.setMilliseconds(milliseconds);
                            }
                        }, hour, minute, true);
                timePicker.show();
            }
        });

        Spinner regLocationPrecision = (Spinner) findViewById(R.id.regLocationPrecision);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distances_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        regLocationPrecision.setAdapter(adapter);

        Spinner regLocationInOut = findViewById(R.id.regLocationInOut);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.in_out_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regLocationInOut.setAdapter(adapter2);

        Spinner regRepeatSelect = findViewById(R.id.regRepeatSelect);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regRepeatSelect.setAdapter(adapter3);

        reminderItem.setDistance(DISTANCES[0]);// TODO
        regLocationPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                reminderItem.setDistance(DISTANCES[pos]);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        Resources res = getResources();
        reminderItem.repeatLabels = res.getStringArray(R.array.repeat_array);
        reminderItem.inOutLabels = res.getStringArray(R.array.in_out_array);
        reminderItem.dayNames = res.getStringArray(R.array.days);

        regRepeatSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                reminderItem.setRepeat(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        inOut = true;
        regLocationInOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                if (pos == 0) {
                    reminderItem.setInOut (ReminderItem.IN);
                } else {
                    reminderItem.setInOut (ReminderItem.OUT);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        regName = findViewById(R.id.regName);
        regNotes = findViewById(R.id.regNotes);

        ScrollMapFragment mapFragment = (ScrollMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.regMap);
        mapFragment.getMapAsync(this);
        mScrollView = (ScrollView) findViewById(R.id.regScrollView);
        mapFragment.setListener(new ScrollMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    public ReminderItem buildReminderItem () {
        reminderItem.setTitle (regName.getText().toString());
        reminderItem.setNotes(regNotes.getText().toString());
        LatLng loc = mMap.getCameraPosition().target;
        reminderItem.setLatLon(loc.latitude, loc.longitude);
        reminderItem.setLocationName(getLocation(loc.latitude, loc.longitude));
        return reminderItem;
    }

    public String getLocation(double lati, double longi){
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
            case R.id.regActionCancel:
                finish();
                return true;

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
        return this.distance;
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
