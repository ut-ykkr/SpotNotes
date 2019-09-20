package org.menhera.spotnotes.ui.reminders_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.menhera.spotnotes.MainActivity;
import org.menhera.spotnotes.R;
import org.menhera.spotnotes.RegisterActivity;
import org.menhera.spotnotes.ReminderItem;
import org.menhera.spotnotes.RemindersAdapter;

import java.util.ArrayList;
import java.util.List;

public class RemindersFragment extends Fragment {

    private RemindersViewModel homeViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RemindersAdapter mAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_reminders_list, container, false);

        FloatingActionButton remlistAddButton = root.findViewById(R.id.remlistAddButton);
        remlistAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        List<ReminderItem> items = new ArrayList<ReminderItem> ();
        for (int i = 0; i < 30; i++) {
            ReminderItem item = new ReminderItem ("ごはん", "2019-09-21 18:00", "東京都文京区");
            items.add (item);
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.remlistRecyclerView);

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
        mAdapter = new RemindersAdapter(items);
        recyclerView.setAdapter(mAdapter);

        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        return root;
    }
}