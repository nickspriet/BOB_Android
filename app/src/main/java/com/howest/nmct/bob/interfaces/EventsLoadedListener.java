package com.howest.nmct.bob.interfaces;

import java.io.IOException;

/**
 * illyism
 * 09/12/15
 */
public interface EventsLoadedListener {
    void startLoading();
    void failedLoading(IOException e);
}
