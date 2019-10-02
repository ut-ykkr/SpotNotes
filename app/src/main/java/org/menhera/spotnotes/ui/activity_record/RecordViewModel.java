package org.menhera.spotnotes.ui.activity_record;

import androidx.lifecycle.ViewModel;

import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Record;

public class RecordViewModel extends ViewModel {
    private Record record;
    SpotNotesRepository repository;

    public RecordViewModel () {
        super();
        record = new Record();
    }

    void setLocation (double latitude, double longitude) {
        record.latitude = latitude;
        record.longitude = longitude;
    }

    void setTitle (String title) {
        record.title = title.trim();
    }

    void setAddress (String address) {
        record.address = address.trim();
    }

    Record getRecord () {
        return record;
    }

    void insertRecord () {
        repository.addRecord(record);
    }
}
