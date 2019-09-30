package org.menhera.spotnotes.ui.reminders_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Reminder;

import java.util.List;

public class RemindersViewModel extends ViewModel {
    LiveData<List<Reminder>> reminders;

    public RemindersViewModel() {

    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }
}