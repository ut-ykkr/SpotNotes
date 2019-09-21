package org.menhera.spotnotes;

public class RecordItem extends ListItem {
    private String title = "";
    private String avgDuration = "";

    public RecordItem(String title, String avgDuration) {
        this.title = title;
        this.avgDuration = avgDuration;
    }

    public String getTitle () {
        return this.title;
    }

    public String getAvgDuration () {
        return this.avgDuration;
    }
}
