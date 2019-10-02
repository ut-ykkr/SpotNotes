package org.menhera.spotnotes.ui.records_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.RecordGroup;
import org.menhera.spotnotes.ui.RecordItem;

import java.util.ArrayList;
import java.util.List;

public class RecordsFragment extends Fragment {

    private RecordsViewModel viewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecordsAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(RecordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_records_list, container, false);
        viewModel.repository = SpotNotesRepository.getInstance(getContext());

        recyclerView = (RecyclerView) root.findViewById(R.id.reclistRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize (true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        // specify an adapter (see also next example)
        mAdapter = new RecordsAdapter();
        recyclerView.setAdapter(mAdapter);

        viewModel.getRecordGroups().observe(this, new Observer<List<RecordGroup>>() {
            @Override
            public void onChanged(List<RecordGroup> recordGroups) {
                mAdapter.setGroups(recordGroups);
            }
        });

        /*final TextView textView = root.findViewById(R.id.text_dashboard);
        viewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}