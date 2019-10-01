package org.menhera.spotnotes.ui.app;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Observer;

import org.menhera.spotnotes.data.Reminder;

import java.util.List;

final public class SpotNotesApplication extends Application {
    AppViewModel viewModel;

    @Override
    public void onCreate() {
        /** Called when the Application-class is first created. */
        super.onCreate();

        viewModel = new AppViewModel(this);

        viewModel.getRepository().getUndeletedReminders().observeForever(new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                Log.d(SpotNotesApplication.class.getName(), "onChanged(), reminders: " + reminders.size());
                for (Reminder reminder: reminders) {
                    viewModel.setReminder(reminder);
                }
            }
        });
    }

    public AppViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onTerminate() {
        /** This Method Called when this Application finished. */
        super.onTerminate();

        Log.d(SpotNotesApplication.class.getName(), "onTerminate()");
    }
}
