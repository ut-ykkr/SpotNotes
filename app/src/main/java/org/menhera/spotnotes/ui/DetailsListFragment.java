package org.menhera.spotnotes.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.menhera.spotnotes.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsListFragment extends Fragment {


    public DetailsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_list, container, false);
    }

}