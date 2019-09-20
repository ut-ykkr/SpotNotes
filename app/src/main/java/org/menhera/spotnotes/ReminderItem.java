package org.menhera.spotnotes;

import java.util.Calendar;

public class ReminderItem extends ListItem {
    private String title = "";
    private String dateTime = "";
    private String location = "";
    private long milliseconds;
    private int repeat;
    private int distance;
    private double lat;
    private double lon;
    private boolean inOut;

    public ReminderItem (String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
    }

    public ReminderItem (String title, Calendar cal, String location, int repeat, int distance, boolean inOut) {
        this.title = title;
        this.location = location;
    }

    public void setLatLon (double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle () {
        return this.title;
    }

    public String getDateTime () {
        return this.dateTime;
    }

    public String getLocation () {
        return this.location;
    }
}
