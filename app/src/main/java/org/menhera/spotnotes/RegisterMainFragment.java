package org.menhera.spotnotes;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.text.DateFormat.MEDIUM;
import static java.text.DateFormat.SHORT;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterMainFragment extends Fragment {
    RegisterActivity activity;
    DatePickerDialog picker;
    TimePickerDialog timePicker;
    Button regDateButton;
    Button regTimeButton;
    Calendar cldr;
    EditText regNotes;

    int year;
    int month;
    int day;
    int hour;
    int minute;

    public RegisterMainFragment() {
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

        regDateButton = getView().findViewById(R.id.regDateButton);
        long milliseconds = activity.getTimeInMillis ();
        String text = DateFormat.getDateInstance().format(new Date(milliseconds));
        regDateButton.setText(text);
        regDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = activity.getYear();
                month = activity.getMonth();
                day = activity.getDay();
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int monthOfYear, int dayOfMonth) {
                                day = dayOfMonth;
                                month = monthOfYear;
                                year = year2;
                                activity.setDate(year2, monthOfYear, dayOfMonth);
                                long milliseconds = activity.getTimeInMillis ();
                                String text = DateFormat.getDateInstance().format(new Date(milliseconds));
                                regDateButton.setText(text);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        regTimeButton = getView().findViewById(R.id.regTimeButton);
        long milliseconds2 = activity.getTimeInMillis ();
        String text2 = DateFormat.getTimeInstance(SHORT).format(new Date(milliseconds2));
        regTimeButton.setText(text2);

        regTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = activity.getHour();
                minute = activity.getMinute();
                timePicker = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                hour = sHour;
                                minute = sMinute;
                                activity.setTime(sHour, sMinute);
                                long milliseconds = activity.getTimeInMillis ();
                                String text = DateFormat.getTimeInstance(SHORT).format(new Date(milliseconds));
                                regTimeButton.setText(text);
                            }
                        }, hour, minute, true);
                timePicker.show();
            }
        });

        Spinner regRepeatSelect = getView().findViewById(R.id.regRepeatSelect);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regRepeatSelect.setAdapter(adapter3);


        regRepeatSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                activity.getReminderItem().setRepeat(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        regNotes = getView().findViewById(R.id.regNotes);
        regNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activity.setNotes (editable.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_main, container, false);
    }

}
