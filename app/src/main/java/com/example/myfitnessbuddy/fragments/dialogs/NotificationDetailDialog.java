package com.example.myfitnessbuddy.fragments.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.interfaces.OnNotificationClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.NotificationDetailDialogBinding;
import com.example.myfitnessbuddy.events.NotificationEvent;
import com.example.myfitnessbuddy.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationDetailDialog extends DialogFragment{

    private TextView title;
    private TextView description;
    private int position;
    private OnNotificationClickedListener listener;
    private String matchedId;
    private String notificationType;

    public NotificationDetailDialog(int position, OnNotificationClickedListener listener, String notificationType) {
        this.position = position;
        this.listener = listener;
        this.notificationType = notificationType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NotificationDetailDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.notification_detail_dialog, container, false);
        View view = binding.getRoot();

        title = binding.notificationDetailTitle;
        description = binding.notificationDetailDescription;
        Button okButton = binding.okNotificationButton;
        Button acceptButton = binding.acceptNotificationButton;
        Button declineButton = binding.declineNotificationButton;

        if (notificationType.equals(Constants.NOTIFYING_TYPE)){
            acceptButton.setVisibility(View.GONE);
            declineButton.setVisibility(View.GONE);
        }
        else if(notificationType.equals(Constants.MATCHING_TYPE)){
            okButton.setVisibility(View.GONE);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.okButtonClicked(position,matchedId);
                dismiss();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.acceptButtonClicked(matchedId);
                dismiss();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.declineButtonClicked(matchedId);
                dismiss();
            }
        });

        return view;
    }




    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationEvent event) {
        Log.i("Noemi","Here");
        title.setText(event.getNotification().getTitle());
        description.setText(event.getNotification().getMessage());
        matchedId = event.getNotification().getMatchedId();
        Log.i("Noemi", event.getNotification().toString());
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
