package com.howest.nmct.bob.models;

import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

/**
 * illyism
 * 21/10/15
 */
public class Ride {
    private String id;
    private String eventId;
    private String title;
    private String date;
    private String image;
    private String address;
    private int requests;
    private List<String> approvedList = new ArrayList<>();
    private User driver;

    public Ride(String id, String eventId, String title, String date, String address) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getApproved() {
        if (approvedList != null) {
            return approvedList.size();
        }
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public Boolean isSelfDriver(Profile profile) {
        return driver.getId().equals(profile.getId());
    }

    public Boolean isApproved(Profile profile) {
        return approvedList != null && approvedList.contains(profile.getId());
    }

    public void setApprovedList(List<String> approvedList) {
        this.approvedList = approvedList;
    }

    public void addApprovedUser(User user) {
        this.approvedList.add(user.getId());
    }

    /**
     * Sets Title, Date, Address for a Ride
     * @param event the event to copy data from
     * @return Ride
     */
    public static Ride createRideFromEvent(Event event) {
        return new Ride("-1", event.getId(), event.getEventName(), event.getEventDate(), event.getEventAddress());
    }


    public static Spanned formatApprovalStatus(Ride ride, Profile profile) {
        if (ride.isSelfDriver(profile)) {
            // I am BOB - so show me approvals and requests
            return Html.fromHtml(String.format("<b>%s</b> approvals • <b>%s</b> requested", ride.getApproved(), ride.getRequests()));
        } else if (ride.isApproved(profile)) {
            // I am not BOB but I'm approved - so show me the amount of guests
            return Html.fromHtml(String.format("<b>%s</b> guests", ride.getApproved()));
        } else {
            // I am not BOB and I'm awaiting approval
            return Html.fromHtml("Waiting for approval...");
        }
    }
}
