package com.howest.nmct.bob.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;

import java.util.ArrayList;
import java.util.LinkedHashSet;

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
    public String toString() {
        if (location == null) {
            return name;
        } else {
            return location.toString();
        }
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

    public static ContentValues[] asContentValues(LinkedHashSet list) {
        ArrayList<ContentValues> values = new ArrayList<>();
        for (Object o : list) {
            ContentValues newValues = null;
            if (o instanceof Ride) {
                Ride r = (Ride) o;
                newValues = asContentValues(r.place);
            }
            if (o instanceof Event) {
                Event e = (Event) o;
                newValues = asContentValues(e.place);
            }
            if (newValues != null) {
                values.add(newValues);
            }
        }
        return values.toArray(new ContentValues[values.size()]);
    }

    private static ContentValues asContentValues(Place p) {
        if (p == null) return null;
        ContentValues values = new ContentValues();
        values.put(PlaceEntry._ID, p.id);
        values.put(PlaceEntry.COLUMN_NAME, p.name);
        Location l = p.location;
        if (l != null) {
            values.put(PlaceEntry.COLUMN_LOCATION_STREET, l.street);
            values.put(PlaceEntry.COLUMN_LOCATION_ZIP, l.zip);
            values.put(PlaceEntry.COLUMN_LOCATION_CITY, l.city);
            values.put(PlaceEntry.COLUMN_LOCATION_COUNTRY, l.country);
            values.put(PlaceEntry.COLUMN_LOCATION_LATITUDE, l.latitude);
            values.put(PlaceEntry.COLUMN_LOCATION_LONGITUDE, l.longitude);
        }
        return values;
    }

}
