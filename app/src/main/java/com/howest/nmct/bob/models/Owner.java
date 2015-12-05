package com.howest.nmct.bob.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * illyism
 * 02/12/15
 */
public class Owner implements Parcelable {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    /**
     *
     * @param id Id of the owner
     * @param name Name of the owner
     */
    public Owner(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    private Owner(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Owner createFromParcel(Parcel in) {
                    return new Owner(in);
                }

                public Owner[] newArray(int size) {
                    return new Owner[size];
                }
            };
}