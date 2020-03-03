package com.example.myfitnessbuddy.events;

public class PassingUserArgumentsEvent {

    String fragmentName;
    String data;
    String additionalData;

    public PassingUserArgumentsEvent(String fragmentName, String data, String additionalData) {
        this.fragmentName = fragmentName;
        this.data = data;
        this.additionalData = additionalData;
    }

    public PassingUserArgumentsEvent(String fragmentName, String data) {
        this.fragmentName = fragmentName;
        this.data = data;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
