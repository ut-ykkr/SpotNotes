package org.menhera.spotnotes.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Reminder.class, Record.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao ();
    public abstract RecordDao recordDao ();
}
