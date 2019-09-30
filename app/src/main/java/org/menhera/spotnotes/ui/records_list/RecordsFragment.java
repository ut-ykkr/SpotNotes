package org.menhera.spotnotes.ui.records_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.ui.RecordActivity;
import org.menhera.spotnotes.ui.RecordItem;
import org.menhera.spotnotes.ui.RecordsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecordsFragment extends Fragment {

    private RecordsViewModel dashboardViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecordsAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(RecordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_records_list, container, false);

        List<RecordItem> items = new ArrayList<RecordItem>();
        for (int i = 0; i < 30; i++) {
            RecordItem item = new RecordItem ("ごはん", "平均所要時間：25.5分");
            items.add (item);
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.reclistRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize (true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        // specify an adapter (see also next example)
        mAdapter = new RecordsAdapter(items);
        recyclerView.setAdapter(mAdapter);

        /*final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}