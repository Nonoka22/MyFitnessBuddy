package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.events.CodeEvent;
import com.example.myfitnessbuddy.R;

import org.greenrobot.eventbus.EventBus;

public class EnterCodeDialog extends DialogFragment implements View.OnClickListener{
    private EditText verificationCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View enterCodeView = inflater.inflate(R.layout.popup_window,null);
        setCancelable(false);
        Button buttonSignIn = enterCodeView.findViewById(R.id.signInButton);
        verificationCode = enterCodeView.findViewById(R.id.fieldLoginVerifCode);
        buttonSignIn.setOnClickListener(this);
        return enterCodeView;
    }

    @Override
    public void onClick(View v) {
        String code = verificationCode.getText().toString();

        if(!code.isEmpty()){
            //Log.i("Noemi", "Verification code entered"+code);
            //communicator.onDialogMessage(code);
            EventBus.getDefault().post(new CodeEvent(code));
            dismiss();
        }
        else{
            //Log.i("Noemi", "No verification code entered");
            Toast.makeText(getContext(), "Please enter the verification code", Toast.LENGTH_SHORT).show();
        }

    }

}
