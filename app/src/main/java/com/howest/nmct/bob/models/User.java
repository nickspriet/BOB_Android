package com.howest.nmct.bob.models;

import java.util.HashMap;

/**
 * illyism
 * 21/10/15
 */
public class User {
    private String id;
    private String name;
    private String profilePicture;
    private String description;
    private String phonenumber;
    private String email;
    private String country;
    private String city;
    private HashMap<String, Boolean> permissions;

    public User(String id) {
        this.id = id;
        this.permissions = new HashMap<>();
    }

    public User(String id, HashMap<String, Boolean> permissions) {
        this.id = id;
        if (permissions.values().size() < 0)
            throw new Error("Need to set permissions for this user");
        this.permissions = permissions;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.permissions = new HashMap<>();
    }

    protected Boolean getPermission(String key) {
        if (permissions.containsKey(key))
            return permissions.get(key);
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDescription() {
        return getPermission(KEY.DESCRIPTION) ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhonenumber() {
        return getPermission(KEY.PHONENUMBER) ? phonenumber : "";
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return getPermission(KEY.EMAIL) ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return getPermission(KEY.COUNTRY) ? country : "";
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return getPermission(KEY.CITY) ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public static class KEY {
        public static String NAME = "NAME";
        public static String PROFILEPICTURE = "PROFILEPICTURE";
        public static String DESCRIPTION = "DESCRIPTION";
        public static String PHONENUMBER = "PHONENUMBER";
        public static String EMAIL = "EMAIL";
        public static String COUNTRY = "COUNTRY";
        public static String CITY = "CITY";
    }


}
