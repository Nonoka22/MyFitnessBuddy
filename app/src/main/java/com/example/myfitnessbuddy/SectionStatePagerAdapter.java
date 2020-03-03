package com.example.myfitnessbuddy;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myfitnessbuddy.fragments.FragmentRegisterBirthday;
import com.example.myfitnessbuddy.fragments.FragmentRegisterGender;
import com.example.myfitnessbuddy.fragments.FragmentRegisterName;
import com.example.myfitnessbuddy.fragments.FragmentRegisterPhoneNumber;
import com.example.myfitnessbuddy.fragments.FragmentRegisterUserType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SectionStatePagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private List<Fragment> fragmentList = new ArrayList<Fragment>(Arrays.asList(new FragmentRegisterPhoneNumber(),new FragmentRegisterName(),new FragmentRegisterBirthday(),new FragmentRegisterGender(),new FragmentRegisterUserType()));

    public SectionStatePagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
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
