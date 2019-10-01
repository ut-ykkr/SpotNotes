package org.menhera.spotnotes.ui.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;

import org.menhera.spotnotes.RemindService;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Reminder;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AppViewModel extends AndroidViewModel {
    private SpotNotesApplication spotNotesApplication;
    private SpotNotesRepository repository;

    public AppViewModel (Application application) {
        super(application);
        spotNotesApplication = (SpotNotesApplication) application;

        repository = SpotNotesRepository.getInstance(application);
    }

    public SpotNotesRepository getRepository() {
        return repository;
    }

    public void setReminder (Reminder reminder) {
        // AlarmManager (id)
        Context context = spotNotesApplication;
        Intent intent = new Intent(context, RemindService.class);
        intent.putExtra(RemindService.ARG_REMINDER_ID, reminder.id);

        // repetition logic here
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.targetBaseTime);

        Calendar currentCalendar = Calendar.getInstance();
        long nextDate;
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);

        // TODO: this is a bit quirky
        switch (reminder.repeat) {
            case REPEAT_NONE:
                nextDate = calendar.getTimeInMillis();
                break;

            case REPEAT_DAY:
                calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.DAY_OF_YEAR, currentCalendar.get(Calendar.DAY_OF_YEAR));
                if (calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                    nextDate = calendar.getTimeInMillis();
                } else {
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                    nextDate = calendar.getTimeInMillis();
                }
                break;

            case REPEAT_WEEK:
                calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.WEEK_OF_YEAR, currentCalendar.get(Calendar.WEEK_OF_YEAR));
                if (calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                    nextDate = calendar.getTimeInMillis();
                } else {
                    calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + 1);
                    nextDate = calendar.getTimeInMillis();
                }
                break;

            case REPEAT_MONTH:
                calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
                if (calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                    nextDate = calendar.getTimeInMillis();
                } else {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                    nextDate = calendar.getTimeInMillis();
                }
                break;

            case REPEAT_YEAR:
                calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
                if (calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                    nextDate = calendar.getTimeInMillis();
                } else {
                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                    nextDate = calendar.getTimeInMillis();
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown repetition");
        }

        PendingIntent pendingIntent
                = PendingIntent.getService(
                context, reminder.id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager
                = (AlarmManager)
                context.getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextDate, pendingIntent);
    }
}
