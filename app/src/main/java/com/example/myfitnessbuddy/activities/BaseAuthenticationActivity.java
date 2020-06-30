package com.example.myfitnessbuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.events.CodeEvent;
import com.example.myfitnessbuddy.fragments.dialogs.EnterCodeDialog;
import com.example.myfitnessbuddy.fragments.dialogs.MessageDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

public abstract class BaseAuthenticationActivity<T extends ViewDataBinding> extends BaseActivity<T> {

    protected String codeSent;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //FragmentManager dialogFragment = getSupportFragmentManager();

    protected void sendVerificationCode(String phoneNumber) {
        Log.i("Noemi","Phone number verification started");
        //phone number verification comes here...

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+40" + phoneNumber,        // Phone number to verify
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
            if(e.getClass().getCanonicalName().equals("FirebaseNetworkException")){
                MessageDialog messageDialog = new MessageDialog("Ooops!","It seems like you are having network issues. Please check your connection!");
                messageDialog.show(dialogFragment, "dialog");
            }
            Log.i("Noemi", e.toString());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i("Noemi", "Phone Verification Completed. Code is sent...");
            //FragmentManager dialogFragment = getSupportFragmentManager();
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
                            signInSuccessful();
                            Log.i("Noemi","Login successful");
                            startActivity(new Intent(getCurrentActivity(), InteriorActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                //open message dialog that informs user about this
                                //FragmentManager dialogFragment = getSupportFragmentManager();
                                MessageDialog messageDialog = new MessageDialog("Invalid code","It seems like you entered the wrong verification code.");
                                messageDialog.show(dialogFragment, "dialog");
                                Log.i("Noemi","Incorrect verification code");
                            }
                            else if(task.getException() != null){
                                MessageDialog messageDialog = new MessageDialog("Unexpected error","Something unexpected happened.");
                                messageDialog.show(dialogFragment, "dialog");
                                Log.i("Noemi","Something happened..." + task.getException());
                            }
                        }
                    }
                });
    }

    protected abstract Activity getCurrentActivity();
    protected abstract void signInSuccessful();

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

}
