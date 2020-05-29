package com.example.myfitnessbuddy.events;

import java.util.ArrayList;

public class PassingTrainerCriteriasEvent extends PassingUserArgumentsEvent {
    public PassingTrainerCriteriasEvent(String fragmentName, ArrayList<String> listData) {
        super(fragmentName, listData);
    }

    public PassingTrainerCriteriasEvent(String fragmentName, String data, String additionalData) {
        super(fragmentName, data, additionalData);
    }

    public PassingTrainerCriteriasEvent(String fragmentName, String data) {
        super(fragmentName, data);
    }
}
