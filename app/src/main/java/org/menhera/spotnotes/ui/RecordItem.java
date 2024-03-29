package org.menhera.spotnotes.ui;

import org.menhera.spotnotes.SpotNotesListItem;

public class RecordItem extends SpotNotesListItem {
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
