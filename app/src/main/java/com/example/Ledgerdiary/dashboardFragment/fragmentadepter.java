package com.example.Ledgerdiary.dashboardFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class fragmentadepter extends FragmentPagerAdapter {
    public fragmentadepter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public fragmentadepter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new onlineuser();
            case 1: return new offlineuser();
            default: return new onlineuser();
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
            title="online";
        }else{
            title="offline";
        }
        return title;
    }
}
