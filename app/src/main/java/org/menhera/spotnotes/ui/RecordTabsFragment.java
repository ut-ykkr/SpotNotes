package org.menhera.spotnotes.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import org.menhera.spotnotes.R;
import org.menhera.spotnotes.ui.records_list.RecordsFragment;
import org.menhera.spotnotes.ui.reminders_list.RemindersFragment;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordTabsFragment extends Fragment {


    public RecordTabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_tabs, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListPagerAdapter pagerAdapter = new ListPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(getString(R.string.tab_by_title), new RecordsFragment());
        pagerAdapter.addFragment(getString(R.string.tab_by_date), new RecordsFragment());
        pagerAdapter.addFragment(getString(R.string.tab_trash), new RecordsFragment());
        ViewPager viewPager = view.findViewById(R.id.recViewPager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.recTabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
