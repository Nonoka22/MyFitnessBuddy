package com.example.myfitnessbuddy.models;

public class NotificationData {

    private String title;
    private String message;
    private String notificationType;
    private String matchedId;

    public NotificationData() {
    }

    public NotificationData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NotificationData(String title, String message, String notificationType) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
    }

    public NotificationData(String title, String message, String notificationType, String receiver) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.matchedId = receiver;
    }

    public String getMatchedId() {
        return matchedId;
    }

    public void setMatchedId(String matchedId) {
        this.matchedId = matchedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NotificationData{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", matchedId='" + matchedId + '\'' +
                '}';
    }
}
