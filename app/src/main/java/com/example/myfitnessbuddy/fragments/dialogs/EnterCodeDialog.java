package com.example.myfitnessbuddy.fragments.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.PopupWindowBinding;
import com.example.myfitnessbuddy.events.CodeEvent;

import org.greenrobot.eventbus.EventBus;

public class EnterCodeDialog extends DialogFragment implements View.OnClickListener{
    private EditText verificationCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PopupWindowBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.popup_window, container, false);
        View view = binding.getRoot();

        setCancelable(false);
        Button buttonSignIn = binding.signInButton;
        verificationCode = binding.fieldLoginVerifCode;
        buttonSignIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String code = verificationCode.getText().toString();

        if(!code.isEmpty()){
            EventBus.getDefault().post(new CodeEvent(code));
            dismiss();
        }
        else{
            Toast.makeText(getContext(), "Please enter the verification code", Toast.LENGTH_SHORT).show();
        }

    }

}
