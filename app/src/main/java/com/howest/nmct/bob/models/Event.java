package com.howest.nmct.bob.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Nick on 28/10/2015.
 */
public class Event implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(eventName);
        dest.writeLong(eventDate.getTime());
        dest.writeString(eventAddress);
        dest.writeString(eventImage);
    }

    public Event(Parcel in) {
        id = in.readString();
        eventName = in.readString();
        eventDate = new Date(in.readLong());
        eventAddress = in.readString();
        eventImage = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Event createFromParcel(Parcel in) {
                    return new Event(in);
                }

                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };
}
