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
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.CriteriasActivity;
import com.example.myfitnessbuddy.events.NotificationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationDetailDialog extends DialogFragment implements View.OnClickListener{

    private TextView title;
    private TextView description;
    //private Notifications notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_detail_dialog,null);

        title = view.findViewById(R.id.notification_detail_title);
        description = view.findViewById(R.id.notification_detail_description);
        Button button = view.findViewById(R.id.notification_button);

        button.setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationEvent event) {
        title.setText(event.getNotification().getTitle());
        description.setText(event.getNotification().getDescription());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), CriteriasActivity.class);
        startActivity(intent);
        dismiss();
    }
}
