package com.howest.nmct.bob.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.api.APIEventsResponse;
import com.howest.nmct.bob.api.APIRidesResponse;
import com.howest.nmct.bob.data.Contracts;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Place;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.models.UserRide;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * illyism
 * 29/12/15
 */
public class BackendSyncAdapter extends AbstractThreadedSyncAdapter {
    private final static String LOG_TAG = BackendSyncAdapter.class.getSimpleName();
    ContentResolver mContentResolver;

    public BackendSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public BackendSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "On Perform Sync");
        getRides();
        getEvents();
    }

    private void getRides() {
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = preferences.getString(BACKEND_TOKEN, "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(BACKEND_SCHEME)
                .encodedAuthority(BACKEND_HOST)
                .appendPath("ride")
                .appendQueryParameter("token", token);

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Rides Call failed", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i(LOG_TAG, responseString);
                APIRidesResponse apiResponse = new Gson().fromJson(responseString, APIRidesResponse.class);
                Log.i(LOG_TAG, apiResponse.data.rides.toString());

                getContext().getContentResolver().bulkInsert(
                        Contracts.EventEntry.CONTENT_URI,
                        Event.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        Contracts.PlaceEntry.CONTENT_URI,
                        Place.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        Contracts.RideEntry.CONTENT_URI,
                        Ride.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        Contracts.UserEntry.CONTENT_URI,
                        User.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        Contracts.UserRideEntry.CONTENT_URI,
                        UserRide.asContentValues(apiResponse.data.rides));
            }
        });
    }

    private void getEvents() {
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = preferences.getString(BACKEND_TOKEN, "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(BACKEND_SCHEME)
                .encodedAuthority(BACKEND_HOST)
                .appendPath("event")
                .appendQueryParameter("token", token);

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Events Call failed", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i(LOG_TAG, responseString);
                APIEventsResponse apiResponse = new Gson().fromJson(responseString, APIEventsResponse.class);
                Log.i(LOG_TAG, apiResponse.data.events.toString());

                getContext().getContentResolver().bulkInsert(
                        Contracts.PlaceEntry.CONTENT_URI,
                        Place.asContentValues(apiResponse.data.events));

                getContext().getContentResolver().bulkInsert(
                        Contracts.EventEntry.CONTENT_URI,
                        Event.asContentValues(apiResponse.data.events));
            }
        });
    }
}
