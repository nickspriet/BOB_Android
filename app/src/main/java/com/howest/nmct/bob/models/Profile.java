package com.howest.nmct.bob.models;

/**
 * illyism
 * 21/10/15
 */
public class Profile extends User {

    public Profile(String id, String name) {
        super(id, name);
    }

    @Override
    protected Boolean getPermission(String key) {
        return true;
    }
}
