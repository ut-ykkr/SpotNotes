package org.menhera.spotnotes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Reminder extends LocationEntity {

    public int radius = 50;

    /**
     * milliseconds that corresponds to Calendar
     */
    @ColumnInfo(name = "target_base_time")
    public long targetBaseTime;

    public enum Repeat {
        REPEAT_NONE,
        REPEAT_DAY,
        REPEAT_WEEK,
        REPEAT_MONTH,
        REPEAT_YEAR,
    };

    public static class RepeatConverter {
        @TypeConverter
        public static int toInt (Repeat repeat) {
            return repeat.ordinal();
        }

        @TypeConverter
        public static Repeat toRepeat (int i) {
            return Repeat.values()[i];
        }
    }

    @TypeConverters(RepeatConverter.class)
    public Repeat repeat = Repeat.REPEAT_NONE;

    public static class InOutConverter {
        @TypeConverter
        public static int toInt (InOut inOut) {
            return inOut.ordinal();
        }

        @TypeConverter
        public static InOut toInOut (int i) {
            return InOut.values()[i];
        }
    }

    public enum InOut {
        IN,
        OUT,
    };

    @TypeConverters(InOutConverter.class)
    @ColumnInfo(name = "in_out")
    public InOut inOut = InOut.IN;
}
