package org.menhera.spotnotes.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.menhera.spotnotes.ui.reminders_list.RemindersFragment;

import java.util.ArrayList;
import java.util.List;

public class ListPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;
    List<String> fragmentNames;

    public ListPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentNames = new ArrayList<>();
    }

    public ListPagerAdapter(FragmentManager fm, int i) {
        super(fm, i);
        fragments = new ArrayList<>();
        fragmentNames = new ArrayList<>();
    }

    public void addFragment(String name, Fragment fragment) {
        fragments.add(fragment);
        fragmentNames.add(name);
    }

    @Override
    public Fragment getItem(int i) {
       return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle (int index) {
//        Fragment fragment = getItem(index);
//        switch (index) {
//            case 0:
//                return fragment.getString()
//        }
        return fragmentNames.get(index);
    }
}
