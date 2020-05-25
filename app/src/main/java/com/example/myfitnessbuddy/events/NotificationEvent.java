package com.example.myfitnessbuddy.events;

import com.example.myfitnessbuddy.models.Notifications;

public class NotificationEvent {

    private Notifications notification;

    public NotificationEvent(Notifications notification) {
        this.notification = notification;
    }

    public Notifications getNotification() {
        return notification;
    }

    public void setNotification(Notifications notification) {
        this.notification = notification;
    }
}
