package com.example.myfitnessbuddy.activities;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityRegistrationBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.RegisterEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.registration.FragmentRegisterBirthday;
import com.example.myfitnessbuddy.fragments.registration.FragmentRegisterGender;
import com.example.myfitnessbuddy.fragments.registration.FragmentRegisterName;
import com.example.myfitnessbuddy.fragments.registration.FragmentRegisterPhoneNumber;
import com.example.myfitnessbuddy.fragments.registration.FragmentRegisterUserType;
import com.example.myfitnessbuddy.models.CustomViewPager;
import com.example.myfitnessbuddy.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class RegistrationActivity extends BaseAuthenticationActivity<ActivityRegistrationBinding> {

    CustomViewPager viewPager;
    TabLayout tabLayout;
    private User user;
    private FirebaseAuth firebaseAuth;
    DatabaseReference dbReference;
    DatabaseReference usersRef;
    ArrayList<Fragment> fragmentList;
    String currentUserId;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initActivityImpl() {
        tabLayout = binding.tabLayoutRegister;
        viewPager = binding.viewPagerRegistration;

        fragmentList = new ArrayList<Fragment>(Arrays.asList(FragmentRegisterPhoneNumber.getInstance(),FragmentRegisterName.getInstance(),
                FragmentRegisterBirthday.getInstance(),FragmentRegisterGender.getInstance(), FragmentRegisterUserType.getInstance()));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference();
        usersRef = dbReference.child(Constants.USERS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = new User();

        setPagerAdapter(viewPager, tabLayout, fragmentList);
    }

    @Override
    protected Activity getCurrentActivity() {
        return RegistrationActivity.this;
    }

    @Override
    protected void signInSuccessful() {
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        Log.i("Noemi","Register cometchat: " + currentUserId);

        //cometChatUser can be created here...
        CometChatUtil.createCometChatUser(currentUserId,user.getFirstName());

        //login CometChatUser
        //CometChatUtil.loginCometChatUser(userId);

        //save user to Firebase database
        usersRef.child(currentUserId).setValue(user);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetNextFragmentEvent showTabEvent){
        setNextFragment(viewPager);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetPrevFragmentEvent showTabEvent){
        setPreviousFragment(viewPager);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingUserArgumentsEvent passingUserArgumentsEvent){
        switch (passingUserArgumentsEvent.getFragmentName()) {
            case "PhoneNumber":
                user.setPhoneNumber(passingUserArgumentsEvent.getData());
                break;
            case "Name":
                user.setFirstName(passingUserArgumentsEvent.getData());
                user.setLastName(passingUserArgumentsEvent.getAdditionalData());
                break;
            case "BirthDate":
                user.setBirthDate(passingUserArgumentsEvent.getData());
                break;
            case "Gender":
                user.setGender(passingUserArgumentsEvent.getData());
                break;
            case "UserType":
                user.setUserType(passingUserArgumentsEvent.getData());
                break;
        }

    }

    @Subscribe
    public void onActionRecieved(RegisterEvent registerEvent){
        sendVerificationCode(user.getPhoneNumber());
    }

}
