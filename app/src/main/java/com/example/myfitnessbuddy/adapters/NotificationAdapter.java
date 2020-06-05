package com.example.myfitnessbuddy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.NotificationRowBinding;
import com.example.myfitnessbuddy.models.Notifications;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notifications> {

    private int resource;

    public NotificationAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Notifications> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String subtitle = getItem(position).getSubtitle();
        String description = getItem(position).getDescription();

        NotificationRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resource, parent, false);
        convertView = binding.getRoot();

        TextView notificationTitle = binding.notificationTitle;
        TextView notificationSubtitle = binding.notificationSubtitle;

        notificationTitle.setText(title);
        notificationSubtitle.setText(subtitle);

        return convertView;
    }
}
