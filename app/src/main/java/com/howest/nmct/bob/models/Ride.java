package com.howest.nmct.bob.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public List<String> approvedList = new ArrayList<>();

    @SerializedName("requests")
    @Expose
    public List<String> requestsList = new ArrayList<>();

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
    private String address;
    private boolean link;

    public Ride(User driver, Event event) {
        this.driver = driver;
        this.event = event;
    }

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
        dest.writeStringList(approvedList);
        dest.writeStringList(requestsList);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeParcelable(place, flags);
        dest.writeParcelable(event, flags);
    }

    public Ride(Parcel in) {
        id = in.readString();
        driver = in.readParcelable(User.class.getClassLoader());
        in.readStringList(approvedList);
        in.readStringList(requestsList);
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

    public Date getStartTime() {
        if (startTime == null) return event.getStartTime();
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(startTime);
        } catch (Exception e) {
            return null;
        }
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

    public String getLink() {
        return Constants.BACKEND_BASEURL + "/ride/" + getId();
    }
}
