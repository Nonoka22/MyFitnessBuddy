package com.example.myfitnessbuddy.models;

public class BuddyRelationshipStatus {

    private String buddyId;
    private String status;

    public BuddyRelationshipStatus(String buddyId, String status) {
        this.buddyId = buddyId;
        this.status = status;
    }

    public BuddyRelationshipStatus() {
    }

    public String getBuddyId() {
        return buddyId;
    }

    public void setBuddyId(String buddyId) {
        this.buddyId = buddyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
