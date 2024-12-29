package com.example.oumarket.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.oumarket.Fragment.HistoryFragment;
import com.example.oumarket.Fragment.OngoingFragment;

public class ViewPagerMyOrderAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMyOrderAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OngoingFragment();
            case 1:
                return new HistoryFragment();
            default:
                return new OngoingFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Đang giao";
                break;
            case 1:
                title = "Lịch sử";
                break;
            default:
                title = "Đang giao";
                break;
        }
        return title;
    }
}
