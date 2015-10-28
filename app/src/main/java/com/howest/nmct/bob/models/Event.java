package com.howest.nmct.bob.models;

/**
 * Created by Nick on 28/10/2015.
 */
public class Event {
    private String id;
    private String eventName;
    private String eventDate;
    private String eventImage;
    private String eventAddress;

    public Event(String id, String eventName, String eventDate, String eventAddress) {
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
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
}
