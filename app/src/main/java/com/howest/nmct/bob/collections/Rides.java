package com.howest.nmct.bob.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;

import java.util.ArrayList;
import java.util.Arrays;

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

        Ride tomorrowLand = new Ride("1", "Tomorrowland", "2 days", "Tomorrowstraat 123, Brussel");
        tomorrowLand.setImage("http://www.tomorrowland.com/sites/default/files/styles/image_gallery_full/public/media/Galleries/2013/BESTOF_Friday_-04.jpg");
        tomorrowLand.setDriver(new User("1", "Ilias Ismanalijev"));
        tomorrowLand.setRequests(2);

        Ride gentseRide = new Ride("2", "Gentse Feesten", "one day", "Korenmarkt, Gent");
        gentseRide.setImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");
        gentseRide.setApprovedList(Arrays.asList("3", "5", "2"));
        gentseRide.setDriver(new User("2", "Nick Spriet"));

        Ride randomRide = new Ride("3", "An event for great and spectacularious amazeballs photography with your fabulous unicorn!!! \uD83D\uDE04 \uD83D\uDC34 \uD83D\uDE04 \uD83D\uDC34 ", "one week", "Somewhere in '); DROP TABLE city;");
        randomRide.setDriver(new User("3", "Bob Dylan"));

        Ride approvedRide = new Ride("4", "Approved event", "2 hours", "Graaf Karel de Goedelaan 38, 8500 Kortrijk");
        approvedRide.setImage("https://lh4.googleusercontent.com/-iPKFd-LgH2o/VME_mbKVl4I/AAAAAAAAABM/_I77Gual3Zs/s408-k-no/");
        approvedRide.setApprovedList(Arrays.asList("1", "2"));
        approvedRide.setDriver(new User("2", "Nick Spriet"));


        Rides.addRides(
                tomorrowLand,
                gentseRide,
                randomRide,
                approvedRide
        );
    }
}
