package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    int count;
    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        count=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChatFragment();
            case 1: {
//                SportsFragment fragment = new SportsFragment();
//                Bundle data = new Bundle();
//                data.putString("Category", "Sports");
//                fragment.setArguments(data);
//                return fragment;
                return new ProfileFragment();
            }
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
