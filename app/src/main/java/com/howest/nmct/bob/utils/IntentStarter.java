package com.howest.nmct.bob.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.howest.nmct.bob.models.User;

import java.util.Locale;

/**
 * illyism
 * 02/12/15
 */
public class IntentStarter {
    public static void openGoogleMaps(Context context, String address) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s", address);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    /**
     * Opens a Facebook profile.
     * Opens the Facebook app if there is one
     * Otherwise, open in the browser
     * @param context context
     * @param facebookUser The User to open the profile for
     */
    public static void openFacebookProfile(Context context, User facebookUser) {
        Intent facebookIntent;

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String facebookScheme = "fb://profile?app_scoped_user_id=" + facebookUser.getFacebookID();
            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
        } catch (Exception e) {
            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUser.getLink()));
        }

        context.startActivity(facebookIntent);
    }
}
