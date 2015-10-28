package com.howest.nmct.bob.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;

import java.util.ArrayList;

/**
 * Created by Nick on 28/10/2015.
 */
public class Events {
    private static ArrayList<Event> events = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
        return events;
    }

    public static Event getEvent(final String id) {
        return Iterables.find(events, new Predicate<Event>() {
            @Override
            public boolean apply(Event input) {
                return input.getId().equals(id);
            }
        });
    }

    public static void addEvent(Event event) {
        events.add(event);
    }

    public static void addEvents(Event... events) {
        for (Event event : events) {
            addEvent(event);
        }
    }

    public static void fetchData() {
        if (events.size() != 0) return;

        Event tomorrowland = new Event("1", "Tomorrowland", "Oct 20 at 9:00pm to Oct 21 at 5:00am", "Tomorrowstraat 123, Brussel");
        tomorrowland.setEventImage("http://www.tomorrowland.com/sites/default/files/styles/image_gallery_full/public/media/Galleries/2013/BESTOF_Friday_-04.jpg");

        Event gentseFeesten = new Event("2", "Gentse Feesten", "Oct 20 at 9:00pm to Oct 21 at 5:00am", "Korenmarkt, Gent");
        gentseFeesten.setEventImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");


        Events.addEvents(
                tomorrowland,
                gentseFeesten,
                tomorrowland,
                gentseFeesten,
                tomorrowland,
                gentseFeesten
        );
    }
}
