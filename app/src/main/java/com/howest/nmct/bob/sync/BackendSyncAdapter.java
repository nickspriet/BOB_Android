package com.howest.nmct.bob.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.api.APIEventsResponse;
import com.howest.nmct.bob.api.APIResponse;
import com.howest.nmct.bob.api.APIRideResponse;
import com.howest.nmct.bob.api.APIRidesResponse;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.data.Contracts.UserRideEntry;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Place;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.models.UserRide;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.howest.nmct.bob.Constants.API_RIDE;
import static com.howest.nmct.bob.Constants.API_RIDE_REQUEST;
import static com.howest.nmct.bob.Constants.API_USER;
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

    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public BackendSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        // Since we've created an account
        BackendSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        // Without calling setSyncAutomatically, our periodic sync will not be enabled.
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        // Finally, let's do a sync to get things started
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
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

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void createRideFromEvent(final Context context, String eventId, final ResponseListener listener) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = new FormEncodingBuilder()
                .add("token", token)
                .add("eventId", eventId)
                .build();

        Request request = new Request.Builder()
                .url(API_RIDE)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Create Ride failed", e);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final APIRideResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIRideResponse.class);

                    context.getContentResolver().insert(RideEntry.CONTENT_URI,
                            Ride.asContentValues(apiResponse.data.ride));

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (apiResponse.statusCode == 200) {
                                listener.onSuccess(apiResponse.data.ride.id);
                            } else {
                                listener.onFailure(new Exception(apiResponse.message));
                            }
                        }
                    });
                } catch (final Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(e);
                        }
                    });
                }
            }
        });
    }

    public static void requestRide(final Context context, final String rideid, final ResponseListener listener) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = new FormEncodingBuilder()
                .add("token", token)
                .add("rideid", rideid)
                .build();

        Request request = new Request.Builder()
                .url(API_RIDE_REQUEST)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Request Ride failed", e);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final APIRideResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIRideResponse.class);

                    context.getContentResolver().delete(RideEntry.CONTENT_URI,
                            RideEntry._ID + "=?",
                            new String[] {rideid}
                    );

                    context.getContentResolver().insert(RideEntry.CONTENT_URI,
                            Ride.asContentValues(apiResponse.data.ride));

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (apiResponse.statusCode == 200) {
                                listener.onSuccess(rideid);
                            } else {
                                listener.onFailure(new Exception(apiResponse.message));
                            }
                        }
                    });
                } catch (final Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(e);
                        }
                    });
                }
            }
        });
    }

    public static void deleteRide(final Context context, final String rideid, final ResponseListener listener) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = new FormEncodingBuilder()
                .add("token", token)
                .add("rideid", rideid)
                .build();

        Request request = new Request.Builder()
                .url(API_RIDE)
                .delete(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Delete Ride failed", e);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final APIResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIResponse.class);

                    context.getContentResolver().delete(RideEntry.CONTENT_URI,
                            RideEntry._ID + "=?",
                            new String[] {rideid}
                    );

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (apiResponse.statusCode == 200) {
                                listener.onSuccess(rideid);
                            } else {
                                listener.onFailure(new Exception(apiResponse.message));
                            }
                        }
                    });
                } catch (final Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(e);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "On Perform Sync");
        getRides();
        getEvents();
    }

    private void getRides() {
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
                APIRidesResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIRidesResponse.class);

                getContext().getContentResolver().bulkInsert(
                        EventEntry.CONTENT_URI,
                        Event.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        PlaceEntry.CONTENT_URI,
                        Place.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        RideEntry.CONTENT_URI,
                        Ride.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        UserEntry.CONTENT_URI,
                        User.asContentValues(apiResponse.data.rides));

                getContext().getContentResolver().bulkInsert(
                        UserRideEntry.CONTENT_URI,
                        UserRide.asContentValues(apiResponse.data.rides));
            }
        });
    }

    private void getEvents() {
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
                APIEventsResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIEventsResponse.class);

                getContext().getContentResolver().bulkInsert(
                        PlaceEntry.CONTENT_URI,
                        Place.asContentValues(apiResponse.data.events));

                getContext().getContentResolver().bulkInsert(
                        EventEntry.CONTENT_URI,
                        Event.asContentValues(apiResponse.data.events));
            }
        });
    }

    public static void syncUser(final Context context, final User newUser) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new Gson().toJson(newUser));

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(API_USER + "/" + newUser.Id)
                .put(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e(LOG_TAG, "Sync User failed", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i(LOG_TAG, response.body().string());
            }
        });
    }
}
