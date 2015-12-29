package com.howest.nmct.bob.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.Constants;
import com.howest.nmct.bob.data.Contracts.RideEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * illyism
 * 21/10/15
 */
public class Ride implements Parcelable {
    @SerializedName("_id")
    @Expose
    public String id;

    @SerializedName("driver")
    @Expose
    public User driver;

    @SerializedName("approved")
    @Expose
    public List<User> approvedList = new ArrayList<>();

    @SerializedName("requests")
    @Expose
    public List<User> requestsList = new ArrayList<>();

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("startTime")
    @Expose
    public String startTime;

    @SerializedName("endTime")
    @Expose
    public String endTime;

    @SerializedName("place")
    @Expose
    public Place place;

    @SerializedName("event")
    @Expose
    public Event event;

    public Ride(User driver, Event event) {
        this.driver = driver;
        this.event = event;
    }

    public Ride() {}

    public static Spanned formatApprovalStatus(Boolean isDriver, Boolean isApproved, int approvedCount, int requestCount) {
        if (isDriver) {
            // I am BOB - so show me approvals and requests
            return Html.fromHtml(String.format("<b>%d</b> approvals • <b>%d</b> requested", approvedCount, requestCount));
        } else if (isApproved) {
            // I am not BOB but I'm approved - so show me the amount of guests
            return Html.fromHtml(String.format("<b>%d</b> riding along", approvedCount));
        } else {
            // I am not BOB and I'm awaiting approval
            return Html.fromHtml("Waiting for approval...");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(driver, flags);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeParcelable(place, flags);
        dest.writeParcelable(event, flags);
    }

    public Ride(Parcel in) {
        id = in.readString();
        driver = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
        event = in.readParcelable(Event.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Ride createFromParcel(Parcel in) {
                    return new Ride(in);
                }

                public Ride[] newArray(int size) {
                    return new Ride[size];
                }
            };

    @Override
    public boolean equals(Object o) {
        Ride otherRide = (Ride) o;
        return otherRide != null && this.id.equals(otherRide.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public static ContentValues[] asContentValues(LinkedHashSet<Ride> rides) {
        ContentValues[] values = new ContentValues[rides.size()];
        int i = 0;
        for (Iterator<Ride> iter = rides.iterator(); iter.hasNext(); i++) {
            values[i] = asContentValues(iter.next());
        }
        return values;
    }

    private static ContentValues asContentValues(Ride r) {
        ContentValues values = new ContentValues();
        values.put(RideEntry._ID, r.id);
        values.put(RideEntry.COLUMN_START_TIME, r.startTime);
        values.put(RideEntry.COLUMN_END_TIME, r.endTime);
        values.put(RideEntry.COLUMN_DESCRIPTION, r.description);
        values.put(RideEntry.COLUMN_DRIVER_ID, r.driver.Id);
        values.put(RideEntry.COLUMN_PLACE_ID, r.place.id);
        values.put(RideEntry.COLUMN_EVENT_ID, r.event.id);
        return values;
    }

    public static String createLink(String rideId) {
        return Constants.BACKEND_BASEURL + "/ride/" + rideId;
    }
}
