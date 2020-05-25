package com.example.myfitnessbuddy.events;

import java.util.ArrayList;

public class PassingUserArgumentsEvent {

    private String fragmentName;
    private String data;
    private String additionalData;
    private ArrayList<String> listData;

    public PassingUserArgumentsEvent(String fragmentName, ArrayList<String> listData) {
        this.fragmentName = fragmentName;
        this.listData = listData;
    }

    public PassingUserArgumentsEvent(String fragmentName, String data, String additionalData) {
        this.fragmentName = fragmentName;
        this.data = data;
        this.additionalData = additionalData;
    }

    public PassingUserArgumentsEvent(String fragmentName, String data) {
        this.fragmentName = fragmentName;
        this.data = data;
    }

    public ArrayList<String> getListData() {
        return listData;
    }

    public void setListData(ArrayList<String> listData) {
        this.listData = listData;
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
