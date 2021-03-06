package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myfitnessbuddy.fragments.interior.TraineeUserProfileFragment;
import com.example.myfitnessbuddy.fragments.interior.TrainerUserProfileFragment;
import com.example.myfitnessbuddy.models.Token;
import com.example.myfitnessbuddy.models.User;
import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityInteriorBinding;
import com.example.myfitnessbuddy.fragments.interior.BuddiesFragment;
import com.example.myfitnessbuddy.fragments.interior.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class InteriorActivity extends BaseActivity<ActivityInteriorBinding> implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    int containerId;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;

    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference currentUserReference;
    private String userType;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_interior;
    }

    @Override
    protected void initActivityImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUserReference = databaseReference.child(Constants.USERS).child(currentUserId);

        currentUserReference.child(Constants.USER_TYPE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        drawerLayout = binding.drawer;
        toolbar = binding.toolbar.toolbar;
        toolbar.setTitle(Constants.HOME);
        navigationView = binding.navigationDrawer;
        containerId = R.id.interior_fragment_container;

        databaseReference.child(Constants.USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                updateToken();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setFragment(Constants.ADD,new HomeFragment(),containerId,toolbar,Constants.HOME);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                setFragment(Constants.REPLACE,HomeFragment.getInstance(),containerId,toolbar,Constants.HOME);
                break;
            case R.id.nav_profile:
                if(userType.equals(Constants.TRAINEE)){
                    setFragment(Constants.REPLACE, TraineeUserProfileFragment.getInstance() ,containerId,toolbar,Constants.PROFILE);
                }
                else if(userType.equals(Constants.TRAINER)){
                    setFragment(Constants.REPLACE, TrainerUserProfileFragment.getInstance() ,containerId,toolbar,Constants.PROFILE);
                }

                break;
            case R.id.nav_buddies:
                setFragment(Constants.REPLACE,BuddiesFragment.getInstance(),containerId,toolbar,Constants.BUDDIES);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                CometChatUtil.logoutCometChat();
                startActivity(new Intent(InteriorActivity.this, MainActivity.class));
                finish();
        }

        return true;
    }

    private void updateToken() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);

        databaseReference.child(Constants.TOKENS).child(firebaseUser.getUid()).setValue(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //call super
        Log.i("Noemi","superinterior");
        super.onActivityResult(requestCode, resultCode, data);


        if(userType.equals(Constants.TRAINEE)){
            TraineeUserProfileFragment fragment = (TraineeUserProfileFragment) getSupportFragmentManager().getFragments().get(0);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        else if(userType.equals(Constants.TRAINER)){
            TrainerUserProfileFragment fragment = (TrainerUserProfileFragment) getSupportFragmentManager().getFragments().get(0);
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }
}
