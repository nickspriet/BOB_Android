package com.howest.nmct.bob.interfaces;

import com.howest.nmct.bob.models.Ride;

import java.util.LinkedHashSet;

/**
 * illyism
 * 09/12/15
 */
public interface RidesLoadedListener {
    void ridesLoaded(LinkedHashSet<Ride> rides);
}
