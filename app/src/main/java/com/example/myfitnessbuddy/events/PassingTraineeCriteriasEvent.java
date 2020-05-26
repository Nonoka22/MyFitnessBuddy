package com.example.myfitnessbuddy.events;

import java.util.ArrayList;

public class PassingTraineeCriteriasEvent extends PassingUserArgumentsEvent {

    public PassingTraineeCriteriasEvent(String fragmentName, ArrayList<String> listData){
        super(fragmentName,listData);
    }

    public PassingTraineeCriteriasEvent(String fragmentName, String data){
        super(fragmentName,data);

    }

    public PassingTraineeCriteriasEvent(String fragmentName, String data, String additionalData) {
        super(fragmentName, data, additionalData);
    }


}
