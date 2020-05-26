package com.example.myfitnessbuddy.models;

public class Price {

    private String hours;
    private String cost;

    public Price() {
    }

    public Price(String hours, String cost) {
        this.hours = hours;
        this.cost = cost;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
