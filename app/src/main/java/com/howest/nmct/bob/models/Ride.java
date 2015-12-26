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

    public Boolean isSelfDriver(User otherUser) {
        return this.getDriver().getId().equals(otherUser.getId());
    }

    public Boolean isApproved(User user) {
        return approvedList != null && approvedList.contains(user.getId());
    }

    public static Spanned formatApprovalStatus(Ride ride, User user) {
        if (ride.isSelfDriver(user)) {
            // I am BOB - so show me approvals and requests
            return Html.fromHtml(String.format("<b>%s</b> approvals • <b>%s</b> requested", ride.getApproved(), ride.getRequests()));
        } else if (ride.isApproved(user)) {
            // I am not BOB but I'm approved - so show me the amount of guests
            return Html.fromHtml(String.format("<b>%s</b> guests", ride.getApproved()));
        } else {
            // I am not BOB and I'm awaiting approval
            return Html.fromHtml("Waiting for approval...");
        }
    }

    public User getDriver() {
        return driver;
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

    public int getApproved() {
        if (approvedList == null) return 0;
        return approvedList.size();
    }

    public int getRequests() {
        if (requestsList == null) return 0;
        return requestsList.size();
    }

    public String getAddress() {
        if (place != null && place.location != null) {
            return place.location.toString();
        }
        return event.getAddress();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        Ride otherRide = (Ride) o;
        return otherRide != null && this.getId().equals(otherRide.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
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
