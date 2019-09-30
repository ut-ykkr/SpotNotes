package org.menhera.spotnotes.ui.app;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import org.menhera.spotnotes.SpotNotesRepository;

public class AppViewModel extends AndroidViewModel {
    private SpotNotesApplication spotNotesApplication;
    private SpotNotesRepository repository;

    public AppViewModel (Application application) {
        super(application);
        spotNotesApplication = (SpotNotesApplication) application;

        repository = new SpotNotesRepository(application);
    }

    public SpotNotesRepository getRepository() {
        return repository;
    }
}
