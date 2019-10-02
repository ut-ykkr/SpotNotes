package org.menhera.spotnotes.ui.details_list;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Record;
import org.menhera.spotnotes.ui.records_list.RecordsAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsListFragment extends Fragment {
    final public static String ARG_NAME = "name";
    final public static String ARG_IS_DELETED = "is_deleted";

    private String name;
    private boolean isDeleted;
    private SpotNotesRepository repository;
    private LiveData<List<Record>> records;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DetailsListAdapter mAdapter;

    public static DetailsListFragment newInstance (String name, boolean isDeleted) {
        DetailsListFragment fragment = new DetailsListFragment();
        Bundle args = new Bundle ();
        args.putString(ARG_NAME, name);
        args.putBoolean(ARG_IS_DELETED, isDeleted);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_details_list, container, false);

        Bundle args = getArguments();
        if (null != args) {
            name = args.getString(ARG_NAME);
            isDeleted = args.getBoolean(ARG_IS_DELETED);
        }

        repository = SpotNotesRepository.getInstance(getContext());

        if (isDeleted) {
            records = repository.getDeletedRecords();
        } else if (null == name) {
            records = repository.getUndeletedRecords();
        } else {
            records = repository.getRecordsByTitle(name);
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.detailsRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        mAdapter = new DetailsListAdapter();
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        records.observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                mAdapter.setRecords(records);
            }
        });
    }
}
