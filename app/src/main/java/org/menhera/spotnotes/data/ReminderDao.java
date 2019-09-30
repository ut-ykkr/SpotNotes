package org.menhera.spotnotes.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao extends LocationEntityDao {
    @Query("SELECT * FROM reminder")
    LiveData<List<Reminder>> getAll ();

    @Query("SELECT * FROM reminder WHERE id IN (:ids)")
    LiveData<List<Reminder>> loadAllByIds (int[] ids);

    @Query("SELECT * FROM reminder WHERE is_deleted = :isDeleted")
    LiveData<List<Reminder>> getAllByDeleted (boolean isDeleted);

    @Query("SELECT * FROM reminder WHERE is_deleted = :isDeleted AND title = :title")
    LiveData<List<Reminder>> getByTitleAndDeleted (String title, boolean isDeleted);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll (Reminder ... reminders);

    @Delete
    void delete (Reminder reminder);

    @Update
    void updateAll (Reminder ... reminders);
}
