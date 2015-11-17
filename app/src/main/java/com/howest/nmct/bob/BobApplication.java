package com.howest.nmct.bob;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * illyism
 * 11/11/15
 */
public class BobApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
