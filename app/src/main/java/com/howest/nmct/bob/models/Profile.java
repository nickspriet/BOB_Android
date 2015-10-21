package com.howest.nmct.bob.models;

/**
 * illyism
 * 21/10/15
 */
public class Profile extends User {

    @Override
    protected Boolean getPermission(String key) {
        return true;
    }
}
