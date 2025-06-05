package com.kholifa.qraccess.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.kholifa.qraccess.activity.HistoryScannerActivity.tab_name;

public class ViewPagerScanAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public ViewPagerScanAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }


    @Override
    public int getItemCount() {
        return tab_name.length;
    }


    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
}
