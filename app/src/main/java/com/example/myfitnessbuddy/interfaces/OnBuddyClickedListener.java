package com.example.myfitnessbuddy.interfaces;

public interface OnBuddyClickedListener {
    void buddyChatClicked(int position, String status);
    void buddyNameClicked(int position);
    void buddyPictureClicked(int position);
    void buddyTrashClicked(int position);
}
