package com.howest.nmct.bob.interfaces;

import com.howest.nmct.bob.models.Event;

import java.util.LinkedHashSet;

/**
 * illyism
 * 09/12/15
 */
public interface EventsLoadedListener {
    void eventsLoaded(LinkedHashSet<Event> events);
}
