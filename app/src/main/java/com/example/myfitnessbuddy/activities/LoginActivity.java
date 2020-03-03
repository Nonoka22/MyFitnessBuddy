package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.events.CodeEvent;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.fragments.EnterCodeDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity /*implements EnterCodeDialog.Communicator*/ {
    EditText phoneField;
    FirebaseAuth firebaseAuth;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();
        //firebaseAuth.setLanguageCode("ro");

        final Button sendCode = findViewById(R.id.sendCodeButton);
        phoneField = findViewById(R.id.fieldLoginPhone);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Noemi", "Here");
                if (!phoneField.getText().toString().isEmpty()) {
                    sendVerificationCode();
                }

            }
        });

    }

    private void sendVerificationCode() {
        Log.i("Noemi","Phone number verification started");
        String phoneNumber = phoneField.getText().toString();
        //Phone number validation. Change this if there is time for it
        if (phoneNumber.length() < 10) {
            phoneField.setError("Please enter a valid phone number");
            phoneField.requestFocus();
            return;
        }

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
                            Log.i("Noemi","Login successful");
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

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
        Log.i("Noemi",event.getCode());
        verifySignInCode(event.getCode());

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

//    @Override
//    public void onDialogMessage(String codeMessage) {
//        verifySignInCode(codeMessage);
//        //Toast.makeText(this, codeMessage, Toast.LENGTH_SHORT).show();
//
//    }
}
