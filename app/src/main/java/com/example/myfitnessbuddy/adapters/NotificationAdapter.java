package com.example.myfitnessbuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.models.Notifications;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Notifications> {

    private Context context;
    private int resource;

    public NotificationAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Notifications> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String subtitle = getItem(position).getSubtitle();
        String description = getItem(position).getDescription();

        Notifications notification = new Notifications(title,subtitle,description);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView notificationTitle = convertView.findViewById(R.id.notification_title);
        TextView notificationSubtitle = convertView.findViewById(R.id.notification_subtitle);

        notificationTitle.setText(title);
        notificationSubtitle.setText(subtitle);

        return convertView;
    }
}
