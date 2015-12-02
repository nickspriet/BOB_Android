package com.howest.nmct.bob.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

    public static void openFacebookProfile(Context context, String facebookUserId) {
        Intent facebookIntent;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String facebookScheme = "fb://profile/" + facebookUserId;
            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
        } catch (Exception e) {
            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=" + facebookUserId));
        }
        context.startActivity(facebookIntent);
    }
}
