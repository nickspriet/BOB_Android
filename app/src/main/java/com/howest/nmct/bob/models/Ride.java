package com.howest.nmct.bob.models;

import java.util.List;

/**
 * illyism
 * 21/10/15
 */
public class Ride {
    private String id;
    private String title;
    private String date;
    private String image;
    private String address;
    private int requests;
    private List<String> approvedList;
    private User driver;

    public Ride(String id, String title, String date, String address) {
        this.id = id;
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
}
