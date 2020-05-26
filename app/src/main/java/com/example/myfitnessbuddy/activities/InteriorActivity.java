package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myfitnessbuddy.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityInteriorBinding;
import com.example.myfitnessbuddy.fragments.interior.BuddiesFragment;
import com.example.myfitnessbuddy.fragments.interior.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InteriorActivity extends BaseActivity<ActivityInteriorBinding> implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    int containerId;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_interior;
    }

    @Override
    protected void initActivityImpl() {
        drawerLayout = binding.drawer;
        toolbar = binding.toolbar.toolbar;
        toolbar.setTitle("");
        navigationView = binding.navigationDrawer;
        containerId = R.id.interior_fragment_container;

        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setFragment(Constants.ADD,new HomeFragment(),containerId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                setFragment(Constants.REPLACE,new HomeFragment(),containerId);
                break;
            case R.id.nav_profile:
                setFragment(Constants.REPLACE,new BuddiesFragment(),containerId);
                break;
            case R.id.nav_buddies:
                setFragment(Constants.REPLACE,new BuddiesFragment(),containerId);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InteriorActivity.this, MainActivity.class));
        }

        return true;
    }
}
