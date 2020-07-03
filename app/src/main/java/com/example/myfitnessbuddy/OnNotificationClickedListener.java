package com.example.myfitnessbuddy;

public interface OnNotificationClickedListener {
    void acceptButtonClicked(String matchedId);
    void declineButtonClicked(String matchedId);
    void okButtonClicked(int position, String matchedId);
    void notificationClicked(int position);
}
