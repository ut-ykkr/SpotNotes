package org.menhera.spotnotes.ui.records_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.RecordGroup;

import java.util.List;

public class RecordsViewModel extends ViewModel {
    SpotNotesRepository repository;
    private MutableLiveData<String> mText;

    public RecordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<RecordGroup>> getRecordGroups () {
        return repository.getRecordGroups();
    }
}