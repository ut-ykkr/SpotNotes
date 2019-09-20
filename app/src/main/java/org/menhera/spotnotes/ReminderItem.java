package org.menhera.spotnotes;

public class ReminderItem extends ListItem {
    private String title = "";
    private String dateTime = "";
    private String location = "";

    public ReminderItem (String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
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
