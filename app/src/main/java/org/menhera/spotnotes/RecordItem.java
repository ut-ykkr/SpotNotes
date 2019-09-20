package org.menhera.spotnotes;

public class RecordItem extends ListItem {
    private String title = "";
    private String avgDuration = "";

    final public int REPEAT_NONE = 0;
    final public int REPEAT_EVERYDAY = 1;
    final public int REPEAT_WEEK = 2;
    final public int REPEAT_MONTH = 3;
    final public int REPEAT_YEAR = 4;

    private int repeat = REPEAT_NONE;

    public RecordItem(String title, String avgDuration) {
        this.title = title;
        this.avgDuration = avgDuration;
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

    public String getTitle () {
        return this.title;
    }

    public String getAvgDuration () {
        return this.avgDuration;
    }
}
