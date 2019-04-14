package com.android.candz.smartmirrorcapstone;

import android.support.annotation.NonNull;

public class EventView {
    private String eventName;
    private String eventLocation;
    private String[] eventTime;

    public EventView(String eventName, String eventLocation, String[] eventTime) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventTime = eventTime;
    }

    public String getTitle() {
        return eventName;
    }

    public String getTime() {
        return eventTime[0] + " - " + eventTime[1];
    }

    @NonNull
    @Override
    public String toString() {
        return eventName + "\n" + eventLocation + "\n" + eventTime[0] + " - " + eventTime[1];
    }
}
