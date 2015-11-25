package com.howest.nmct.bob.collections;

import com.howest.nmct.bob.models.Event;

import java.util.ArrayList;
import java.util.Date;

/**
 * Nick on 28/10/2015.
 */
public class Events {
    private static final ArrayList<Event> events = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
        return events;
    }

    public static Event getEvent(final String id) {
        Event foundEvent = null;

        for (Event e : events) {
            if (e.getId().equals(id)) {
                foundEvent = e;
                break;
            }
        }

        return foundEvent;
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

        Event tomorrowland = new Event("1", "Tomorrowland", new Date(2015, 7, 24, 12, 0), "Schommelei, 2850 Boom");
        tomorrowland.setEventImage("https://scontent-bru2-1.xx.fbcdn.net/hphotos-xta1/v/t1.0-9/10947321_10152829530009177_4534804258545577034_n.png?oh=9d7fe71ca09119d2f2f9481ced70a372&oe=56E4CDA7");

        Event gentseFeesten = new Event("2", "Gentse Feesten", new Date(2015, 8, 13, 12, 7), "Korenmarkt, 9000 Gent");
        gentseFeesten.setEventImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");

        Event kiekenfuif = new Event("3", "Kiekenfuif", new Date(12, 11, 9, 9, 0), "Howest Obee - Graaf Karel de Goedelaan 5, 8500 Kortrijk");
        kiekenfuif.setEventImage("https://scontent-bru2-1.xx.fbcdn.net/hphotos-xta1/t31.0-8/12079829_907950152574754_6218674705680497012_o.jpg");

        Event campusdagHowest = new Event("4", "Campusdag Howest", new Date(2014, 4, 26, 9, 0), "Howest - Graaf Karel de Goedelaan 5, 8500 Kortrijk");
        campusdagHowest.setEventImage("https://scontent-bru2-1.xx.fbcdn.net/hphotos-xta1/t31.0-8/s2048x2048/1932576_10152127788918106_4924855659205408870_o.jpg");

        Event kulakKerstbal = new Event("5", "Campusdag Howest", new Date(2015, 12, 4, 10, 0), "Waregem expo - Zuiderlaan 20, 8790 Waregem");
        kulakKerstbal.setEventImage("https://scontent-bru2-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/12188928_1518477605141668_219865140803850367_n.jpg?oh=03d8d196cc7163b8dff568167c479a14&oe=56F6461D");

        Events.addEvents(
                tomorrowland,
                gentseFeesten,
                kiekenfuif,
                campusdagHowest,
                kulakKerstbal
        );
    }
}
