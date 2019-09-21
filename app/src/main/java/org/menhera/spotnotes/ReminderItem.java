package org.menhera.spotnotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderItem extends ListItem {
    private String title = "";
    private String dateTime = "";
    private long milliseconds;

    private int distance = 0;

    private double lat;
    private double lon;

    final public static boolean IN = true;
    final public static boolean OUT = false;

    private boolean inOut = IN;

    final public static int REPEAT_NONE = 0;
    final public static int REPEAT_EVERYDAY = 1;
    final public static int REPEAT_WEEK = 2;
    final public static int REPEAT_MONTH = 3;
    final public static int REPEAT_YEAR = 4;

    private int repeat = REPEAT_NONE;

    private String notes;

    private String locationName;

    String dateFormat;
    protected String[] repeatLabels = {};
    protected String[] inOutLabels = {};
    protected String[] dayNames = {};

    public ReminderItem (String title, String dateTime, String locationName) {
        this.title = title;
        this.dateTime = dateTime;
        this.locationName = locationName;
    }

    public ReminderItem (String title, String locationName) {
        this.title = title;
        this.locationName = locationName;
    }

    public void setInOut (boolean inOut1) {
        inOut = inOut1;
    }

    public void setTitle (String title1) {
        title = title1;
    }

    public boolean getInOut () {
        return inOut;
    }

    public void setNotes (String notes) {
        this.notes = notes;
    }

    public void setMilliseconds (long milliseconds1) {
        milliseconds = milliseconds1;
    }

    public long getMilliseconds () {
        return milliseconds;
    }

    public String getNotes() {
        return this.notes;
    }

    public double getLat () {
        return this.lat;
    }

    public double getLon () {
        return this.lon;
    }

    public void setLatLon (double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setDistance (int distance) {
        this.distance = distance;
    }

    public int getDistance () {
        return this.distance;
    }

    public void setRepeat (int repeat) {
        switch (repeat) {
            case REPEAT_EVERYDAY:
                this.repeat = REPEAT_EVERYDAY;
                break;

            case REPEAT_WEEK:
                this.repeat = REPEAT_WEEK;
                break;

            case REPEAT_MONTH:
                this.repeat = REPEAT_MONTH;
                break;

            case REPEAT_YEAR:
                this.repeat = REPEAT_YEAR;
                break;

            default:
                this.repeat = REPEAT_NONE;
        }
    }

    public int getRepeat () {
        return this.repeat;
    }

    public String getTitle () {
        return this.title;
    }

    public String getDateTime () {
        if (null != this.dateTime) {
            return this.dateTime;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        dateTime = "";
        switch (this.repeat) {
            case REPEAT_NONE:
                dateFormat = "yyyy-MM-dd hh:mm";
                break;

            case REPEAT_EVERYDAY:
                dateFormat = "hh:mm";
                break;

            case REPEAT_WEEK:
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                switch (day) {
                    case Calendar.SUNDAY:
                        day = 0;
                        break;

                    case Calendar.MONDAY:
                        day = 1;
                        break;

                    case Calendar.TUESDAY:
                        day = 2;
                        break;

                    case Calendar.WEDNESDAY:
                        day = 3;
                        break;

                    case Calendar.THURSDAY:
                        day = 4;
                        break;

                    case Calendar.FRIDAY:
                        day = 5;
                        break;

                    default:
                        day = 6;
                }
                if (dayNames.length <= day) {
                    dateTime = "";
                } else {
                    dateTime = dayNames[day] + " ";
                }
                dateFormat = "hh:mm";
                break;

            case REPEAT_MONTH:
                dateFormat = "dd hh:mm";
                break;

            case REPEAT_YEAR: default:
                dateFormat = "MM-dd hh:mm";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        dateTime += sdf.format(calendar.getTime());

        if (repeatLabels.length > repeat) {
            dateTime += " ";
            dateTime += repeatLabels[repeat];
        }

        return dateTime;
    }

    public String getLocation () {
        String location = "";
        location += this.locationName;
        location += " ";
        String distanceName = distance + "m";
        location += distanceName;
        if (inOutLabels.length >= 2) {
            location += "\n";
            if (IN == inOut) {
                location += inOutLabels[0];
            } else {
                location += inOutLabels[1];
            }
        }
        return location;
    }
}
