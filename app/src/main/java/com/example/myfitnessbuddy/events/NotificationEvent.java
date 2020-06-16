package com.example.myfitnessbuddy.events;

import com.example.myfitnessbuddy.models.NotificationData;

public class NotificationEvent {

    private NotificationData notification;
    private int position;

    public NotificationEvent(NotificationData notification, int position) {
        this.notification = notification;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NotificationData getNotification() {
        return notification;
    }

    public void setNotification(NotificationData notification) {
        this.notification = notification;
    }
}
