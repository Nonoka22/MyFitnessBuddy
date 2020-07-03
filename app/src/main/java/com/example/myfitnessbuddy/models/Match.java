package com.example.myfitnessbuddy.models;

public class Match {
    private String traineeId;
    private String trainerId;
    private int priority;
    private String status;

    public Match() {
    }

    public Match(String traineeId, String trainerId, int priority, String status) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.priority = priority;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
