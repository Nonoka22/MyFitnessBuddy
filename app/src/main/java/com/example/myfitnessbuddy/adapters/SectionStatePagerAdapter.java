package com.example.myfitnessbuddy.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


public class SectionStatePagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private List<Fragment> fragmentList;

    public SectionStatePagerAdapter(FragmentManager fm, int numOfTabs, List<Fragment> fragmentList) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
