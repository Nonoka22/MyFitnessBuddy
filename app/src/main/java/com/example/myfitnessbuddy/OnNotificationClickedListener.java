package com.example.myfitnessbuddy;

public interface OnNotificationClickedListener {
    void acceptButtonClicked(int position, String matchedId);
    void declineButtonClicked(int position, String matchedId);
    void okButtonClicked(int position, String matchedId);
    void notificationClicked(int position);
}
