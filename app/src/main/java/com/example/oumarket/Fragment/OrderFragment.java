package com.example.oumarket.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oumarket.Adapter.ViewPagerMyOrderAdapter;
import com.example.oumarket.R;
import com.google.android.material.tabs.TabLayout;

public class OrderFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

        ViewPagerMyOrderAdapter viewPageMyOrderAdapter = new ViewPagerMyOrderAdapter(requireActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager2.setAdapter(viewPageMyOrderAdapter);
            tabLayout.setupWithViewPager(viewPager2);

        return view;
    }
}