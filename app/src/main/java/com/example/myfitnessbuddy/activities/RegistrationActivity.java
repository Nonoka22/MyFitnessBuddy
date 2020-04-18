package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.models.CustomViewPager;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.models.SectionStatePagerAdapter;
import com.example.myfitnessbuddy.events.CodeEvent;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.RegisterEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.events.ShowTabEvent;
import com.example.myfitnessbuddy.fragments.EnterCodeDialog;
import com.example.myfitnessbuddy.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.myfitnessbuddy.SwipeDirection.none;

public class RegistrationActivity extends AppCompatActivity  {

    CustomViewPager viewPager;
    TabLayout tabLayout;
    TabItem tabPhoneNum;
    TabItem tabName;
    TabItem tabBirthDate;
    TabItem tabGender;
    TabItem tabUserType;
    private String codeSent;
    private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;
    DatabaseReference usersRef;
    Map<String, User> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tabLayout = findViewById(R.id.tabLayout);
        tabPhoneNum = findViewById(R.id.tabPhoneNum);
        tabName = findViewById(R.id.tabName);
        tabBirthDate = findViewById(R.id.tabDate);
        tabGender = findViewById(R.id.tabGender);
        tabUserType = findViewById(R.id.tabUserType);
        viewPager = findViewById(R.id.viewPager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference();
        usersRef = dbReference.child("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        user = new User();

        hideTabItemAt(0);
        hideTabItemAt(1);
        hideTabItemAt(2);
        hideTabItemAt(3);
        hideTabItemAt(4);

        SectionStatePagerAdapter pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAllowedSwipeDirection(none);

        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int numTab = tab.getPosition();
                        viewPager.setCurrentItem(numTab);
                    }
                });

    }

    private void sendVerificationCode() {
        Log.i("Noemi","Phone number verification started");
        //Phone number validation needed here....
        String phoneNumber = user.getPhoneNumber();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.i("Noemi", "Phone Verification Completed");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("Noemi", e.toString());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i("Noemi", "Phone Verification Completed. Code is sent...");
            FragmentManager dialogFragment = getSupportFragmentManager();
            EnterCodeDialog enterCodeDialog = new EnterCodeDialog();
            enterCodeDialog.show(dialogFragment, "dialog");
            codeSent = s;
        }
    };

    private void verifySignInCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            users.put(currentUser.getUid(), user);
                            usersRef.setValue(users);

                            Log.i("Noemi","Reg and Login successful");
                            startActivity(new Intent(RegistrationActivity.this, InteriorActivity.class));

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.i("Noemi","Incorrect verification code");
                            }
                            else if( task.getException() instanceof Exception){
                                Log.i("Noemi","Something happened..." + task.getException());
                            }
                        }
                    }
                });
    }

    @Subscribe
    public void onMessageEvent(CodeEvent event) {
       // Log.i("Noemi",event.getCode());
        verifySignInCode(event.getCode());

    }

    public void hideTabItemAt(int position){
        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position).setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void onSomeActionEventRecieved(ShowTabEvent showTabEvent){
        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(showTabEvent.getPosition()).setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetNextFragmentEvent showTabEvent){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetPrevFragmentEvent showTabEvent){
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingUserArgumentsEvent passingUserArgumentsEvent){
        switch (passingUserArgumentsEvent.getFragmentName()) {
            case "PhoneNumber":
                user.setPhoneNumber(passingUserArgumentsEvent.getData());
                //Log.i("Noemi", user.toString());
                break;
            case "Name":
                user.setFirstName(passingUserArgumentsEvent.getData());
                user.setLastName(passingUserArgumentsEvent.getAdditionalData());
               // Log.i("Noemi", user.toString());
                break;
            case "BirthDate":
                user.setBirthDate(passingUserArgumentsEvent.getData());
               // Log.i("Noemi", user.toString());
                break;
            case "Gender":
                user.setGender(passingUserArgumentsEvent.getData());
                //Log.i("Noemi", user.toString());
                break;
            case "UserType":
                user.setUserType(passingUserArgumentsEvent.getData());
                break;
        }

        //sendVerificationCode(user.getPhoneNumber());
        Log.i("Noemi",user.toString());
    }

    @Subscribe
    public void onActionRecieved(RegisterEvent registerEvent){
        sendVerificationCode();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
