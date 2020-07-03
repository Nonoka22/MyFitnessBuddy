package com.example.myfitnessbuddy.fragments.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.InteriorActivity;
import com.example.myfitnessbuddy.activities.LoginActivity;
import com.example.myfitnessbuddy.databinding.DialogMessageBinding;
import com.example.myfitnessbuddy.utils.Constants;

public class MessageDialog extends DialogFragment implements View.OnClickListener {

    private String title,text;

    public MessageDialog(String title, String text) {
        this.title = title;
        this.text = text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogMessageBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_message, container, false);
        View view = binding.getRoot();

        setCancelable(false);

        TextView titleTV = binding.messageDialogTitle;
        TextView textTV = binding.messageDialogText;
        Button okButton = binding.messageDialogButton;

        titleTV.setText(title);
        textTV.setText(text);
        okButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(title.equals(Constants.KNOWN_PHONE_NUMBER_TITLE)){
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        else if(title.equals(Constants.MINOR_TITLE)){
            System.exit(0);
        }
        else if(title.equals(Constants.TRAINER_CANNOT_CHAT_TITLE)){
            startActivity(new Intent(getContext(), InteriorActivity.class));
        }
        dismiss();
    }
}
