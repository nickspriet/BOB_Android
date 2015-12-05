package com.howest.nmct.bob.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * illyism
 * 02/12/15
 */
public class Place implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("location")
    @Expose
    public Location location;

    /**
     *
     * @param id Id of the place
     * @param location Location of the place
     * @param name Name of the place
     */
    public Place(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(location, flags);
    }

    private Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Place createFromParcel(Parcel in) {
                    return new Place(in);
                }

                public Place[] newArray(int size) {
                    return new Place[size];
                }
            };
}
