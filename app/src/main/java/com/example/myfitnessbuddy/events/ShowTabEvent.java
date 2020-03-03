package com.example.myfitnessbuddy.events;

public class ShowTabEvent {
    private int position;

    public ShowTabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
