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

    @Query("SELECT * FROM record WHERE is_deleted = :isDeleted")
    LiveData<List<Record>> getAllByDeleted (boolean isDeleted);

    @Query("SELECT * FROM record WHERE is_deleted = :isDeleted AND title = :title")
    LiveData<List<Record>> getByTitleAndDeleted (String title, boolean isDeleted);

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
