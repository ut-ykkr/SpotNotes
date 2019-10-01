package org.menhera.spotnotes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.TextView;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Record;
import org.menhera.spotnotes.data.Reminder;

import java.util.List;

public class StatsActivity extends AppCompatActivity {
    SpotNotesRepository repository;
    TextView statsText;
    int undeletedReminders;
    int deletedReminders;
    int undeletedRecords;
    int deletedRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats2);

        repository = SpotNotesRepository.getInstance(this);
        statsText = findViewById(R.id.statsText);

        repository.getUndeletedReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                undeletedReminders = reminders.size();
                showStats();
            }
        });

        repository.getDeletedReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                deletedReminders = reminders.size();
                showStats();
            }
        });

        repository.getUndeletedRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                undeletedRecords = records.size();
                showStats();
            }
        });

        repository.getDeletedRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                deletedRecords = records.size();
                showStats();
            }
        });

    }

    private void showStats () {
        String text = "";
        text += "Reminders in use: " + undeletedReminders;
        text += "\nReminders marked as deleted: " + deletedReminders;
        text += "\nRecords in use: " + undeletedRecords;
        text += "\nRecords marked as deleted: " + deletedRecords;
        statsText.setText(text);
    }
}
