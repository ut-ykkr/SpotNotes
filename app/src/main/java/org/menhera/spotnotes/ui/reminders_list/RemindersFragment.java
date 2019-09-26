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
import org.menhera.spotnotes.SpotNotesApplication;
import org.menhera.spotnotes.SpotNotesListItem;

import java.util.ArrayList;
import java.util.List;

public class RemindersFragment extends Fragment implements SpotNotesApplication.ItemsChangeListener {
    final private static String ARG_TYPE = "type";
    final public static int ARG_TYPE_INUSE = 1;
    final public static int ARG_TYPE_TRASH = 2;

    private int type = ARG_TYPE_INUSE;
    private RemindersViewModel homeViewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RemindersAdapter mAdapter;
    private boolean started = false;
    private List<ReminderItem> items;

    public static RemindersFragment newInstance (int type) {
        RemindersFragment fragment = new RemindersFragment();
        Bundle args = new Bundle ();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_reminders_list, container, false);

        Bundle args = getArguments();
        if (null != args) {
            type = args.getInt(ARG_TYPE);
        }

        FloatingActionButton remlistAddButton = root.findViewById(R.id.remlistAddButton);

        remlistAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });



//        for (int i = 0; i < 30; i++) {
//            ReminderItem item = new ReminderItem ("ごはん", "2019-09-21 18:00", "東京都文京区");
//            items.add (item);
//        }

        recyclerView = (RecyclerView) root.findViewById(R.id.remlistRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize (true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        SpotNotesApplication app = (SpotNotesApplication) getActivity().getApplication();


        switch (type) {
            case ARG_TYPE_INUSE:
                items = app.getReminderItems();
                app.registerReminderItemsListener(this);
                break;

            case ARG_TYPE_TRASH:
                items = app.getReminderTrashItems();
                app.registerReminderTrashItemsListener(this);
                remlistAddButton.hide();
                break;

            default:
                throw new UnsupportedOperationException("Unsupported type of reminders list");
        }

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



        started = true;
        return root;
    }

    public void setItems (List<ReminderItem> items) {
        if (!started) return;
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    public void onItemsChange (List<? extends SpotNotesListItem> items) {
        setItems((List<ReminderItem>) items);
    }

    public void onResume () {
        super.onResume();
        if (!started) return;

    }
}