package org.menhera.spotnotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.menhera.spotnotes.data.AppDatabase;
import org.menhera.spotnotes.data.Record;
import org.menhera.spotnotes.data.RecordDao;
import org.menhera.spotnotes.data.RecordGroup;
import org.menhera.spotnotes.data.Reminder;
import org.menhera.spotnotes.data.ReminderDao;

import java.util.List;

public class SpotNotesRepository {
    private static SpotNotesRepository instance;

    private final static String DB_NAME = "spotnotes-db";

    AppDatabase db;
    ReminderDao reminderDao;
    RecordDao recordDao;

    LiveData<List<Reminder>> undeletedReminders;
    LiveData<List<Reminder>> deletedReminders;

    LiveData<List<Record>> undeletedRecords;
    LiveData<List<Record>> deletedRecords;
    LiveData<List<RecordGroup>> recordGroups;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIEW `RecordGroup` AS SELECT avg(duration) AS avg_duration, title, avg(latitude) AS avg_latitude, avg(longitude) AS avg_longitude, sum(duration) AS total_duration FROM record GROUP BY title");
        }
    };

    public static SpotNotesRepository getInstance(Context context) {
        Context app = context.getApplicationContext();
        if (null == instance) {
            instance = new SpotNotesRepository(app);
        }
        return instance;
    }

    private SpotNotesRepository (Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addMigrations(MIGRATION_1_2).build();
        reminderDao = db.reminderDao();
        recordDao = db.recordDao();

        undeletedReminders = reminderDao.getAllByDeleted(false);
        deletedReminders = reminderDao.getAllByDeleted(true);
        undeletedRecords = recordDao.getAllByDeleted(false);
        deletedRecords = recordDao.getAllByDeleted(true);
        recordGroups = recordDao.getAllGroupsByDeleted(false);
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

    public LiveData<List<RecordGroup>> getRecordGroups() {
        return recordGroups;
    }

    public LiveData<List<Record>> getRecordsByTitle (String title) {
        return recordDao.getByTitleAndDeleted(title, false);
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

    public void markReminderDeleted (Reminder ... reminders) {
        new AsyncTask<Reminder, Void, Void>() {
            @Override
            public Void doInBackground (Reminder ... reminders) {
                int[] ids = new int[reminders.length];
                for (int i = 0; i < reminders.length; i++) {
                    ids[i] = reminders[i].id;
                }
                reminderDao.markAllDeletedByIds(ids);
                return null;
            }
        }.execute(reminders);
    }

    public void deleteReminder (Reminder ... reminders) {
        new AsyncTask<Reminder, Void, Void>() {
            @Override
            public Void doInBackground (Reminder ... reminders) {
                reminderDao.deleteAll(reminders);
                return null;
            }
        }.execute(reminders);
    }

    public void addRecord (Record ... records) {
        new AsyncTask<Record, Void, Void>() {
            @Override
            public Void doInBackground (Record ... records) {
                recordDao.insertAll(records);
                return null;
            }
        }.execute(records);
    }
}
