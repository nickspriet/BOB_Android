package com.howest.nmct.bob.interfaces;

/**
 * illyism
 * 09/12/15
 */
public interface ResponseListener {
    void onSuccess(String id);
    void onFailure(Exception e);
}
