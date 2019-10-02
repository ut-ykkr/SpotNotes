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
public interface RecordDao extends LocationEntityDao {
    @Query("SELECT * FROM record")
    LiveData<List<Record>> getAll ();

    @Query("SELECT * FROM record WHERE id IN (:ids)")
    LiveData<List<Record>> loadAllByIds (int[] ids);

    @Query("SELECT * FROM record WHERE is_deleted = :isDeleted ORDER BY finished_time DESC")
    LiveData<List<Record>> getAllByDeleted (boolean isDeleted);

    @Query("SELECT * FROM record WHERE is_deleted = :isDeleted AND title = :title")
    LiveData<List<Record>> getByTitleAndDeleted (String title, boolean isDeleted);

    @Query("SELECT avg(duration) AS avg_duration, title, avg(latitude) AS avg_latitude, avg(longitude) AS avg_longitude, sum(duration) AS total_duration FROM record WHERE is_deleted = :isDeleted GROUP BY title")
    LiveData<List<RecordGroup>> getAllGroupsByDeleted (boolean isDeleted);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll (Record ... records);

    @Delete
    void delete (Record record);

    @Update
    void updateAll (Record ... records);

    @Query("UPDATE OR REPLACE record SET is_deleted = 1 WHERE id IN (:ids)")
    void markAllDeletedByIds (int[] ids);

    @Query("UPDATE OR REPLACE record SET is_deleted = 0 WHERE id IN (:ids)")
    void markAllUndeletedByIds (int[] ids);
}
