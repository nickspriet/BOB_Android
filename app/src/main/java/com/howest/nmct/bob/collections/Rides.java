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
        User user = new User("1", "1", "Ilias Ismanalijev", "Ilias", "Ismanalijev",
                "https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xap1/v/t1.0-9/12027626_1077838225560816_4235616874323113303_n.jpg?oh=e0e858a8876f3a49b69bc0690064fc27&oe=56B28D3B&__gda__=1454655490_c13993560b66647cf06de663af6b237c",
                "https://scontent-bru2-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/11846638_1053922741285698_465322780535143622_n.jpg?oh=f34b22f77aa08411af8da81729063ba6&oe=56B5A67B", "" +
                "https://www.facebook.com/IliasIsmanalijev");
        Ride ride =new Ride("1", "1", user, "Kiekenfuif", "this friday", "Howest, Kortrijk");
        ride.setImage("https://scontent-bru2-1.xx.fbcdn.net/hphotos-xta1/v/t1.0-9/12141664_907950152574754_6218674705680497012_n.jpg?oh=20ed26455077e67f417f909220c3aa6f&oe=56F58065");
        addRide(ride);
    }
}
