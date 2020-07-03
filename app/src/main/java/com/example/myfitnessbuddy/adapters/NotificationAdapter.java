package com.example.myfitnessbuddy.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.interfaces.OnNotificationClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.NotificationRowBinding;
import com.example.myfitnessbuddy.models.NotificationData;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

   private List<NotificationData> notifications;
   private NotificationRowBinding binding;
   private OnNotificationClickedListener listener;

    public NotificationAdapter(List<NotificationData> notifications, OnNotificationClickedListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.notification_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());

        Log.i("Noemi", "oncreatviewholder");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.notificationClicked(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationData notification = notifications.get(position);
        holder.bindViewHolder(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = binding.notificationTitle;
            text = binding.notificationText;
        }

        void bindViewHolder(NotificationData notification){
            title.setText(notification.getTitle());
            text.setText(StringUtils.abbreviate(notification.getMessage(), 15));
        }
    }
}
