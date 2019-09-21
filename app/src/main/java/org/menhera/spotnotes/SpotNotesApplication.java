package org.menhera.spotnotes;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

final public class SpotNotesApplication extends Application {
    List<ReminderItem> reminderItems;

    @Override
    public void onCreate() {
        /** Called when the Application-class is first created. */
        super.onCreate();
        reminderItems = new ArrayList<>();
    }

    @Override
    public void onTerminate() {
        /** This Method Called when this Application finished. */
        super.onTerminate();

    }

    public int addReminderItem (ReminderItem reminderItem) {
        int index = size ();
        reminderItems.add(reminderItem);
        return index;
    }

    public List<ReminderItem> getReminderItems () {
        return reminderItems;
    }

    public int size () {
        return reminderItems.size();
    }

    public ReminderItem get (int index) {
        return reminderItems.get(index);
    }
}
