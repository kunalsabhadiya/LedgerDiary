package com.example.Ledgerdiary.reminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class reminderfragmentadepter extends FragmentPagerAdapter {

    public reminderfragmentadepter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public reminderfragmentadepter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new occurance();
            case 1: return new singletimeentry();
            default: return new occurance();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if(position==0){
            title="Occurance";
        }else{
            title="onetime";
        }
        return title;
    }
}

