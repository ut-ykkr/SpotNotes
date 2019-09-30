package org.menhera.spotnotes.data;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "is_deleted")
    public boolean isDeleted;

    public String title = "";

    public String notes = "";

    public double latitude = Double.NaN;

    public double longitude = Double.NaN;

    public String address = "";

    public boolean hasValidLocation () {
        return !(Double.isNaN(latitude) || Double.isNaN(longitude));
    }
}
