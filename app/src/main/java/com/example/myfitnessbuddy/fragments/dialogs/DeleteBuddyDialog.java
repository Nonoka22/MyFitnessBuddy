package com.example.myfitnessbuddy.fragments.dialogs;

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
import com.example.myfitnessbuddy.databinding.DialogDeleteBuddyBinding;
import com.example.myfitnessbuddy.events.CodeEvent;
import com.example.myfitnessbuddy.events.DeleteBuddyEvent;

import org.greenrobot.eventbus.EventBus;

public class DeleteBuddyDialog extends DialogFragment {

    String buddyName;

    public DeleteBuddyDialog(String buddyName) {
        this.buddyName = buddyName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogDeleteBuddyBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_delete_buddy, container, false);
        View view = binding.getRoot();

        setCancelable(false);

        TextView textTV = binding.deleteBuddyDialogText;
        Button yesButton = binding.deleteBuddyYesButton;
        Button noButton = binding.deleteBuddyNoButton;

        textTV.setText("Are you sure, you want to delete " + buddyName + "?");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteBuddyEvent());
                dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


}
