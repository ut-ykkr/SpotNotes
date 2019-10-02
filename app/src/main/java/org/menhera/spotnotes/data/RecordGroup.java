package org.menhera.spotnotes.data;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Query;

/**
 * Records grouped by title.
 */
@DatabaseView("SELECT avg(duration) AS avg_duration, title, avg(latitude) AS avg_latitude, avg(longitude) AS avg_longitude, sum(duration) AS total_duration FROM record GROUP BY title")
public class RecordGroup {
    public String title;

    @ColumnInfo(name = "avg_duration")
    public long avgDuration;

    @ColumnInfo(name = "total_duration")
    public long totalDuration;

    @ColumnInfo(name = "avg_latitude")
    public double avgLatitude;

    @ColumnInfo(name = "avg_longitude")
    public double avgLongitude;
}
