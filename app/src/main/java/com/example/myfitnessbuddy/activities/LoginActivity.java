package com.example.myfitnessbuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityLoginBinding;
import com.example.myfitnessbuddy.fragments.dialogs.MessageDialog;
import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends BaseAuthenticationActivity<ActivityLoginBinding>  {

    FirebaseAuth firebaseAuth;
    String phoneNumber;
    Button sendCodeButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    boolean phoneNumberExists = false;
    TextView errorMessage;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initActivityImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        sendCodeButton = binding.sendCodeButton;
        errorMessage = binding.errorMessageLogin;

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = binding.fieldLoginPhone.getText().toString();

                ConnectivityManager cm =
                        (ConnectivityManager)getCurrentActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(!isConnected){
                    MessageDialog messageDialog = new MessageDialog("Network issue","Please check your connection");
                    messageDialog.show(dialogFragment, "dialog");
                }else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot : dataSnapshot.child(Constants.USERS).getChildren()){
                                if(snapshot.child(Constants.PHONE_NUMBER).getValue().toString().equals(phoneNumber)){
                                    phoneNumberExists = true;
                                    break;
                                }
                            }
                            if (!phoneNumber.isEmpty()) {
                                errorMessage.setVisibility(View.GONE);
                                if(phoneNumberExists){
                                    sendVerificationCode(phoneNumber);
                                }
                                else if(!phoneNumberExists && (phoneNumber.length() == 9 || phoneNumber.length() == 10)){
                                    MessageDialog messageDialog = new MessageDialog("We don't know you","Please sign up first!");
                                    messageDialog.show(dialogFragment, "dialog");
                                }
                                else if(phoneNumber.length() < 9){
                                    errorMessage.setVisibility(View.VISIBLE);
                                    errorMessage.setText("Invalid phone number...");
                                }
                            }
                            else {
                                errorMessage.setVisibility(View.VISIBLE);
                                errorMessage.setText("You must enter your phone number");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("Noemi","Database error: " + databaseError);
                        }
                    });
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
