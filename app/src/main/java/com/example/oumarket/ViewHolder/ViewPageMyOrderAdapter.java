package com.example.oumarket.ViewHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.oumarket.ui.my_order_page.HistoryFragment;
import com.example.oumarket.ui.my_order_page.OngoingFragment;

public class ViewPageMyOrderAdapter extends FragmentStatePagerAdapter {

    public ViewPageMyOrderAdapter(@NonNull FragmentManager fm, int behavior) {
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
                title = "Ongoing";
                break;
            case 1:
                title = "History";
                break;
            default:
                title = "Ongoing";
                break;
        }
        return title;
    }
}
