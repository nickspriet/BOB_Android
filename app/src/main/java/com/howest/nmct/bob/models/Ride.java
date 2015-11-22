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

    public Ride(String id, String eventId, User user, String title, String date, String address) {
        this.id = id;
        this.eventId = eventId;
        this.driver = user;
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

    public Boolean isSelfDriver(User otherUser) {
        return this.getDriver().getId().equals(otherUser.getId());
    }

    public Boolean isApproved(User user) {
        return approvedList != null && approvedList.contains(user.getId());
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
     * @param driver The driver of the new ride
     * @return Ride
     */
    public static Ride createRideFromEvent(Event event, User driver) {
        Ride ride = new Ride("-1", event.getId(), driver, event.getEventName(), event.getEventDate().toString(), event.getEventAddress());
        ride.addApprovedUser(driver);
        return ride;
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
}
