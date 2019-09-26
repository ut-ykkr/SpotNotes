package org.menhera.spotnotes.ui;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import org.menhera.spotnotes.R;

public class MainSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
