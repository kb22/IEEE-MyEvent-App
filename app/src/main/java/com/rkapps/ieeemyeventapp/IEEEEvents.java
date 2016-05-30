package com.rkapps.ieeemyeventapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sony on 5/26/2016.
 */
public class IEEEEvents {
    String eventName;
    String description;
    int eventID;
    public String date1;

    IEEEEvents(String eventName, String description, String date1, int eventID){
        this.eventName = eventName;
        this.description = description;
        this.eventID = eventID;
        this.date1 = date1;
    }

    private List<IEEEEvents> events;
    private void initialize(){
        events = new ArrayList<>(); // Check

    }

}

