package org.menhera.spotnotes;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import org.menhera.spotnotes.data.AppDatabase;
import org.menhera.spotnotes.data.Record;
import org.menhera.spotnotes.data.RecordDao;
import org.menhera.spotnotes.data.Reminder;
import org.menhera.spotnotes.data.ReminderDao;

import java.util.List;

public class SpotNotesRepository {
    private final static String DB_NAME = "spotnotes-db";

    AppDatabase db;
    ReminderDao reminderDao;
    RecordDao recordDao;

    LiveData<List<Reminder>> undeletedReminders;
    LiveData<List<Reminder>> deletedReminders;

    LiveData<List<Record>> undeletedRecords;
    LiveData<List<Record>> deletedRecords;

    public SpotNotesRepository (Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        reminderDao = db.reminderDao();
        recordDao = db.recordDao();

        undeletedReminders = reminderDao.getAllByDeleted(false);
        deletedReminders = reminderDao.getAllByDeleted(true);
        undeletedRecords = recordDao.getAllByDeleted(false);
        deletedRecords = recordDao.getAllByDeleted(true);
    }

    public LiveData<List<Reminder>> getUndeletedReminders() {
        return undeletedReminders;
    }

    public LiveData<List<Reminder>> getDeletedReminders() {
        return deletedReminders;
    }

    public LiveData<List<Record>> getUndeletedRecords() {
        return undeletedRecords;
    }

    public LiveData<List<Record>> getDeletedRecords() {
        return deletedRecords;
    }

    public LiveData<List<Reminder>> getRemindersByIds (int[] ids) {
        return reminderDao.loadAllByIds(ids);
    }

    /**
     * Insert or update a reminder item.
     * @param reminders
     */
    public void addReminder (Reminder ... reminders) {
        new AsyncTask<Reminder, Void, Void>() {
            @Override
            public Void doInBackground (Reminder ... reminders) {
                reminderDao.insertAll(reminders);
                return null;
            }
        }.execute(reminders);
    }
}
