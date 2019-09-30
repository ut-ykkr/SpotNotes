package org.menhera.spotnotes.ui.activity_register;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.menhera.spotnotes.data.Reminder;

import java.util.Calendar;
import java.util.List;

public class RegisterViewModel extends ViewModel {
    private LiveData<List<Reminder>> reminders;
    private Reminder reminder;

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    public void setReminders(LiveData<List<Reminder>> reminders) {
        this.reminders = reminders;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public long getTimeInMillis () {
        return reminder.targetBaseTime;
    }

    public void setTimeInMillis (long millis) {
        reminder.targetBaseTime = millis;
    }

    public Calendar getCalendar () {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimeInMillis ());
        return cal;
    }

    public int getDay () {
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth () {
        return getCalendar().get(Calendar.MONTH);
    }

    public int getYear () {
        return getCalendar().get(Calendar.YEAR);
    }

    public int getMinute () {
        return getCalendar().get(Calendar.MINUTE);
    }

    public int getHour () {
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public void setTime (int hour, int minute) {
        Calendar cal = getCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        this.reminder.targetBaseTime = cal.getTimeInMillis();
    }

    public void setDate (int year, int month, int day) {
        Calendar cal = getCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        this.reminder.targetBaseTime = cal.getTimeInMillis();
    }

    public void setNotes (String notes) {
        this.reminder.notes = notes;
    }

    public void setTitle (String title) {
        this.reminder.title = title;
    }

    public void setRepeat (Reminder.Repeat repeat) {
        this.reminder.repeat = repeat;
    }

    public void setRadius (int radius) {
        this.reminder.radius = radius;
    }

    public void setInOut (Reminder.InOut inOut) {
        this.reminder.inOut = inOut;
    }

    public void setLocation (double latitude, double longitude) {
        this.reminder.latitude = latitude;
        this.reminder.longitude = longitude;
    }

    public void setAddress (String address) {
        this.reminder.address = address;
    }

    public double getLatitude () {
        return this.reminder.latitude;
    }

    public double getLongitude () {
        return this.reminder.longitude;
    }

    public String getAddress () {
        return this.reminder.address;
    }

    public String getTitle () {
        return this.reminder.title;
    }

    public String getNotes () {
        return this.reminder.notes;
    }

    public int getRadius () {
        return this.reminder.radius;
    }

    public Reminder.Repeat getRepeat () {
        return this.reminder.repeat;
    }

    public Reminder.InOut getInOut () {
        return this.reminder.inOut;
    }
}
