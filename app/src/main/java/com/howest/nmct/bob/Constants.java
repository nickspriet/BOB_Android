package com.howest.nmct.bob;

/**
 * illyism
 * 17/11/15
 */
public class Constants {
    static String BACKEND_HOST = "192.168.1.227:3000";
    static String BACKEND_SCHEME = "http";
    static String BACKEND_BASEURL =  BACKEND_SCHEME + "://" + BACKEND_HOST;
    static String API_USER_LOGIN = BACKEND_BASEURL + "/user/login";
    static String API_USER_PROFILE = BACKEND_BASEURL + "/user/profile";

    static String FACEBOOK_TOKEN = "com.howest.nmct.bob.FACEBOOK_TOKEN";
    static String FACEBOOK_USERID = "com.howest.nmct.bob.FACEBOOK_USERID";
    static String FACEBOOK_PERMISSIONS = "com.howest.nmct.bob.FACEBOOK_PERMISSIONS";
    static String FACEBOOK_EXPIRES = "com.howest.nmct.bob.FACEBOOK_EXPIRES";

    static String BACKEND_TOKEN = "com.howest.nmct.bob.BACKEND_TOKEN";
    static String DEVICE_TYPE = "com.howest.nmct.bob.DEVICE_TYPE";
    static String DEVICE_MODEL = "com.howest.nmct.bob.DEVICE_MODEL";

    static String USER_PROFILE = "com.howest.nmct.bob.USER_PROFILE";
}
