package com.example.myfitnessbuddy.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myfitnessbuddy.models.Token;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.i("Noemi","Notifriactions ids ");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        if(firebaseUser != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Token token1 = new Token(refreshToken);

        Task<Void> firebaseDatabase = FirebaseDatabase.getInstance().getReference(Constants.TOKENS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
}
