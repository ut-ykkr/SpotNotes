package org.menhera.spotnotes.ui.activity_register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import android.util.Log;
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

import org.menhera.spotnotes.LocationClient;
import org.menhera.spotnotes.R;
import org.menhera.spotnotes.RemindService;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Reminder;
import org.menhera.spotnotes.ui.ListPagerAdapter;
import org.menhera.spotnotes.ui.app.SpotNotesApplication;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class RegisterActivity extends AppCompatActivity {
    final public static String ARG_REMINDER_ID = "reminder_id";

    private RegisterViewModel viewModel;

    EditText regName;

    // @todo move things into fragments and support classes
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        viewModel.setReminders(new MutableLiveData<List<Reminder>>());
        if (null == viewModel.getReminder()) {
            try {
                int id = savedInstanceState.getInt(ARG_REMINDER_ID);
                if (0 == id) {
                    throw new NullPointerException("Empty id");
                }

                SpotNotesApplication app = (SpotNotesApplication) getApplication();
                int[] ids = {id};

                viewModel.setReminders(app.getViewModel().getRepository().getRemindersByIds(ids));
                viewModel.getReminders().observe(this, new Observer<List<Reminder>>() {
                    @Override
                    public void onChanged(List<Reminder> reminders) {
                        viewModel.setReminder(reminders.get(0));
                        viewModel.getReminders().removeObserver(this);
                    }
                });

                throw new NullPointerException("Reminder is null");
            } catch (Exception ex) {
                viewModel.setReminder(new Reminder());
                viewModel.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            }
        }

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

        regName = findViewById(R.id.regName);
        regName.setText(viewModel.getTitle());
        viewModel.getReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                regName.setText(reminders.get(0).title);
                viewModel.getReminders().removeObserver(this);
            }
        });
    }

    public RegisterViewModel getViewModel() {
        return viewModel;
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
                complete ();
//
//                // AlarmManager (index)
//                Context context = getBaseContext();
//                Intent intent = new Intent(context, RemindService.class);
//                intent.putExtra("index", index);
//                PendingIntent pendingIntent
//                        = PendingIntent.getService(
//                        context, -1, intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                AlarmManager alarmManager
//                        = (AlarmManager)
//                        context.getSystemService(ALARM_SERVICE);
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderItem.getMilliseconds(), pendingIntent);
//                finish ();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void complete () {
        viewModel.setTitle(regName.getText().toString());
        viewModel.setAddress(getLocationName(viewModel.getLatitude(), viewModel.getLongitude()));

        Reminder reminder = viewModel.getReminder();
        if (TextUtils.isEmpty(reminder.title)) {
            Log.d (RegisterActivity.class.getName(), "Title is empty");
            regName.setError(getResources().getString(R.string.required));
            return;
        }

        SpotNotesApplication app = (SpotNotesApplication) getApplication();
        app.getViewModel().getRepository().addReminder(reminder);

        finish();
    }
}
