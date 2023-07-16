package com.example.abortion;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    @Override
    public int getCount() {
        // Return the number of tabs
        return 3;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the corresponding Fragment based on the position
        switch (position) {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ResourceFragment2();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title for each tab based on the position
        switch (position) {
            case 0:
                return "Answers";
            case 1:
                return "Resources";
            case 2:
                return "Info";
            default:
                return null;
        }
    }
}