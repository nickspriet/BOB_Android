package com.howest.nmct.bob.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * illyism
 * 02/12/15
 */
public class Location implements Parcelable {
    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("latitude")
    @Expose
    public float latitude;

    @SerializedName("longitude")
    @Expose
    public float longitude;

    @SerializedName("street")
    @Expose
    public String street;

    @SerializedName("zip")
    @Expose
    public String zip;

    /**
     *
     * @param zip Zipcode of the location
     * @param street Street of the location
     * @param longitude Longitude of the location
     * @param latitude Latitude of the location
     * @param country Country of the location
     * @param city City of the location
     */
    public Location(String city, String country, float latitude, float longitude, String street, String zip) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.zip = zip;
    }

    @Override
    public String toString() {
        if (street == null || street.isEmpty()) {
            return city + ", " + country;
        }
        return street + ", \n" + city + ", " + country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(country);;
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        dest.writeString(street);
        dest.writeString(zip);
    }

    private Location(Parcel in) {
        city = in.readString();
        country = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        street = in.readString();
        zip = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Location createFromParcel(Parcel in) {
                    return new Location(in);
                }

                public Location[] newArray(int size) {
                    return new Location[size];
                }
            };
}
