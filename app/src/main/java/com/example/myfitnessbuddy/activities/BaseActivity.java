package com.example.myfitnessbuddy.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfitnessbuddy.adapters.SectionStatePagerAdapter;
import com.example.myfitnessbuddy.models.CustomViewPager;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.myfitnessbuddy.SwipeDirection.none;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {

    protected V binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(binding == null){
            binding = DataBindingUtil.setContentView(this,getActivityLayout());
        }
        initActivityImpl();
    }

    protected abstract int getActivityLayout();
    protected abstract void initActivityImpl();

    protected void setPagerAdapter(final CustomViewPager viewPager, TabLayout tabLayout, ArrayList<Fragment> fragmentList){
        SectionStatePagerAdapter pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAllowedSwipeDirection(none);

        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

    }

    protected void setFragment(String action,Fragment fragment, int container, Toolbar toolbar,String title){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(action.equals(Constants.REPLACE)){
            fragmentTransaction.replace(container,fragment);
        }
        else if(action.equals(Constants.ADD)){
            fragmentTransaction.add(container,fragment);
        }

        fragmentTransaction.commit();
        toolbar.setTitle(title);
    }

    protected void setNextFragment(CustomViewPager viewPager){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    protected void setPreviousFragment(CustomViewPager viewPager){
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }


}
