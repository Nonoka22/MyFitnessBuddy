package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initActivityImpl() {

        CometChatUtil.initCometChat(getApplicationContext());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            // User is signed in
            startActivity(new Intent(MainActivity.this, InteriorActivity.class));

        } else {
            // No user is signed in
            Log.i("Noemi", "User is not signed in");
        }
    }
}
