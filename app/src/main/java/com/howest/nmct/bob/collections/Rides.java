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

    public static void fetchData() {
        if (rides.size() != 0) return;

        Ride tomorrowLand = new Ride("1", "Tomorrowland", "20/10/2015", "Tomorrowstraat 123, Brussel");
        tomorrowLand.setImage("http://www.tomorrowland.com/sites/default/files/styles/image_gallery_full/public/media/Galleries/2013/BESTOF_Friday_-04.jpg");

        Ride gentseRide = new Ride("2", "Gentse Feesten", "12/07/2016", "Korenmarkt, Gent");
        gentseRide.setImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");
        gentseRide.setApproved(10);
        tomorrowLand.setRequests(5);

        Ride randomRide = new Ride("3", "An event for great and spectacularious amazeballs photography with your fabulous unicorn!!! \uD83D\uDE04 \uD83D\uDC34 \uD83D\uDE04 \uD83D\uDC34 ", "12/12/2020", "Somewhere in '); DROP TABLE city;");

        Rides.addRides(
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                randomRide
        );
    }
}
