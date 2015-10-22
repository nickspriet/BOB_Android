package com.howest.nmct.bob.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.howest.nmct.bob.models.Ride;

import java.util.ArrayList;

/**
 * illyism
 * 22/10/15
 */
public class Rides {
    private static ArrayList<Ride> rides = new ArrayList<>();

    public static ArrayList<Ride> getRides() {
        return rides;
    }

    public static void addRide(Ride ride) {
        rides.add(ride);
    }

    public static void addRides(Ride... rides) {
        for (Ride ride : rides) {
            addRide(ride);
        }
    }

    public static Ride getRide(final String id) {
        return Iterables.find(rides, new Predicate<Ride>() {
            @Override
            public boolean apply(Ride input) {
                return input.getId().equals(id);
            }
        });
    }

}
