package com.howest.nmct.bob.models;

/**
 * illyism
 * 21/10/15
 */
public class Ride {
    private String title;
    private String date;
    private String image;

    public Ride(String title, String date, String image) {
        this.title = title;
        this.date = date;
        this.image = image;
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
}
