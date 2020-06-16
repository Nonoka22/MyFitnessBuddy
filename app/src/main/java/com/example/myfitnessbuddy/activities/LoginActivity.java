package com.example.myfitnessbuddy.activities;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityLoginBinding;
import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends BaseAuthenticationActivity<ActivityLoginBinding>  {

    FirebaseAuth firebaseAuth;
    String phoneNumber;
    Button sendCodeButton;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initActivityImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        sendCodeButton = binding.sendCodeButton;

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = binding.fieldLoginPhone.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    sendVerificationCode(phoneNumber);
                }
            }
        });
    }

    @Override
    protected Activity getCurrentActivity() {
        return LoginActivity.this;
    }

    @Override
    protected void signInSuccessful() {
        Log.i("Noemi","Login cometchat: " + firebaseAuth.getCurrentUser().getUid());
        CometChatUtil.loginCometChatUser(firebaseAuth.getCurrentUser().getUid());
    }
}
