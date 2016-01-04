package com.howest.nmct.bob;

/**
 * illyism
 * 17/11/15
 */
public class Constants {
    public static final String BACKEND_HOST = "bob.il.ly";
    public static final String BACKEND_SCHEME = "http";
    public static final String BACKEND_BASEURL =  BACKEND_SCHEME + "://" + BACKEND_HOST;
    public static final String API_USER_LOGIN = BACKEND_BASEURL + "/user/login";
    public static final String API_RIDE_CREATE = BACKEND_BASEURL + "/ride";
    public static final String API_RIDE_REQUEST = BACKEND_BASEURL + "/ride/request";

    public static final String FACEBOOK_TOKEN = "com.howest.nmct.bob.FACEBOOK_TOKEN";
    public static final String FACEBOOK_USERID = "com.howest.nmct.bob.FACEBOOK_USERID";
    public static final String FACEBOOK_PERMISSIONS = "com.howest.nmct.bob.FACEBOOK_PERMISSIONS";

    public static final String USER_ID = "com.howest.nmct.bob.USER_ID";
    public static final String FACEBOOK_EXPIRES = "com.howest.nmct.bob.FACEBOOK_EXPIRES";
    public static final String BACKEND_TOKEN = "com.howest.nmct.bob.BACKEND_TOKEN";
    public static final String DEVICE_TYPE = "com.howest.nmct.bob.DEVICE_TYPE";

    public static final String DEVICE_MODEL = "com.howest.nmct.bob.DEVICE_MODEL";
    public static final String USER_PROFILE = "com.howest.nmct.bob.USER_PROFILE";
    public static final String EVENT = "com.howest.nmct.bob.EVENT";

    public static final String RIDE = "com.howest.nmct.bob.RIDE";
    public final static String ACTIVITY_EVENTS = "com.howest.nmct.bob.ACTIVITY_EVENTS";
    public final static String ACTIVITY_FEED = "com.howest.nmct.bob.ACTIVITY_FEED";
    public final static String ACTIVITY_PROFILE = "com.howest.nmct.bob.ACTIVITY_PROFILE";
    public final static String ACTIVITY_SETTINGS = "com.howest.nmct.bob.ACTIVITY_SETTINGS";
    public final static String ACTIVITY_RIDES = "com.howest.nmct.bob.ACTIVITY_RIDES";

    public final static int REQUEST_EDIT = 1000;
    public final static int REQUEST_RIDE = 1010;
    public final static int RESULTS_CLOSE = 0;
    public final static int RESULTS_OK = 1;

    public final static String APPBAR_TRANSITION_NAME = "appbar";
    public final static String TOOLBAR_TRANSITION_NAME = "toolbar";
}
