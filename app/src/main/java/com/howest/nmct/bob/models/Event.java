package com.howest.nmct.bob.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nick on 28/10/2015.
 */
public class Event {
    private String id;
    private String eventName;
    private Date eventDate;
    private String eventImage;
    private String eventAddress;
    private int eventInfo;
    private int eventFriendsOrGuests;

    public Event(String id, String eventName, Date eventDate, String eventAddress) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventAddress = eventAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }


    public String getEventDateFormat(String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.US);
        return sdf.format(this.getEventDate());
    }

    public String getEventFriendsOrGuests() {
        return "Ilias, Nick and 5 other friends are going";
    }
}
