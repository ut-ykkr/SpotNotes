package org.menhera.spotnotes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Location-aware time record.
 * Note that when there is a pause, finishedTime - startTime != duration.
 */
@Entity
public class Record extends LocationEntity {
    /**
     * Date in milliseconds
     */
    @ColumnInfo(name = "start_time")
    public long startTime;

    /**
     * Date in milliseconds
     */
    @ColumnInfo(name = "finished_time")
    public long finishedTime;

    /**
     * Total duration in milliseconds
     */
    public long duration;
}
