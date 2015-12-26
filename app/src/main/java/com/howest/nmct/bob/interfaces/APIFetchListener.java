package com.howest.nmct.bob.interfaces;

import java.io.IOException;

/**
 * illyism
 * 09/12/15
 */
public interface APIFetchListener{
    void startLoading();
    void failedLoading(IOException e);
}
