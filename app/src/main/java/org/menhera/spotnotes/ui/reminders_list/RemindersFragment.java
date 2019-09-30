package org.menhera.spotnotes.ui.reminders_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.SpotNotesRepository;
import org.menhera.spotnotes.data.Reminder;
import org.menhera.spotnotes.ui.activity_register.RegisterActivity;
import org.menhera.spotnotes.ui.ReminderItem;

import java.security.InvalidParameterException;
import java.util.List;

public class RemindersFragment extends Fragment  {

    /**
     * Which of the two lists we are in, i.e. in-use list or trash.
     */
    public enum Type {
        IN_USE,
        DELETED,
    };

    final private static String ARG_TYPE = "deletion_status";
    private Type type = Type.IN_USE;

    private RemindersViewModel viewModel;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RemindersAdapter mAdapter;

    public static RemindersFragment newInstance (Type type) {
        RemindersFragment fragment = new RemindersFragment();
        Bundle args = new Bundle ();
        args.putInt(ARG_TYPE, type.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel =
                ViewModelProviders.of(this).get(RemindersViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_reminders_list, container, false);

        Bundle args = getArguments();
        if (null != args) {
            type = Type.values()[args.getInt(ARG_TYPE)];
        }

        if (null == viewModel.reminders) {
            SpotNotesRepository repository = SpotNotesRepository.getInstance(getActivity().getApplication());
            switch (type) {
                case IN_USE:
                    viewModel.reminders = repository.getUndeletedReminders();
                    break;

                case DELETED:
                    viewModel.reminders = repository.getDeletedReminders();
                    break;

                default:
                    throw new InvalidParameterException("Invalid type");
            }
        }


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

        // specify an adapter (see also next example)
        mAdapter = new RemindersAdapter();
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(RemindersFragment.class.getName(), "observe(): " + viewModel.getReminders().hashCode());
        viewModel.getReminders().observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                Log.d(RemindersFragment.class.getName(), "onChanged()");
                mAdapter.setReminders(reminders);
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();

        Log.d(RemindersFragment.class.getName(), "observers: " + viewModel.getReminders().hasActiveObservers());
        List<Reminder> reminders = viewModel.getReminders().getValue();
        if (null == reminders) {
            Log.d(RemindersFragment.class.getName(), "reminders == null");
        } else {
            Log.d(RemindersFragment.class.getName(), "reminders != null");
            mAdapter.setReminders(reminders);
        }
    }
}