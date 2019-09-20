package org.menhera.spotnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int distance; // meters
    DatePickerDialog picker;
    TimePickerDialog timePicker;
    Button regDateButton;
    Button regTimeButton;
    final int[] DISTANCES = {50, 100, 200, 500, 1000, 5000, 10000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Reminder");

        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);

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
                                regDateButton.setText(year + "-" + (month + 1 ) + "-" + day);
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
                                regTimeButton.setText(hour + ":" + minute);
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

        distance = DISTANCES[0];
        regLocationPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                distance = DISTANCES[pos];
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
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
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
}
