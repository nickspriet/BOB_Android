package com.howest.nmct.bob.models;

import android.content.ContentValues;

import com.howest.nmct.bob.data.Contracts.UserRideEntry;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * illyism
 * 26/12/15
 */
public class UserRide {

    public static final int APPROVED = 1;
    public static final int REQUEST = 2;

    public static ContentValues[] asContentValues(LinkedHashSet<Ride> rides) {
        ArrayList<ContentValues> values = new ArrayList<>();
        for (Ride r : rides) {
            values.addAll(asContentValues(r));
        }
        return values.toArray(new ContentValues[values.size()]);
    }

    private static ContentValues asContentValues(User user, Ride ride, int approved) {
        ContentValues values = new ContentValues();
        values.put(UserRideEntry.COLUMN_RIDE_ID, ride.id);
        values.put(UserRideEntry.COLUMN_STATUS, approved);
        values.put(UserRideEntry.COLUMN_USER_ID, user.Id);
        return values;
    }

    public static ArrayList<ContentValues> asContentValues(Ride r) {
        ArrayList<ContentValues> values = new ArrayList<>();

        for (User u : r.approvedList) {
            ContentValues listValues = asContentValues(u, r, UserRide.APPROVED);
            if (listValues != null) values.add(listValues);
        }
        for (User u : r.requestsList) {
            ContentValues listValues = asContentValues(u, r, UserRide.REQUEST);
            if (listValues != null) values.add(listValues);
        }
        if (r.driver != null) {
            ContentValues driverValues = asContentValues(r.driver, r, UserRide.APPROVED);
            if (driverValues != null) values.add(driverValues);
        }

        return values;
    }
}
