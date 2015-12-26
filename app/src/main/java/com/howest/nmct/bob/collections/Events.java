package com.howest.nmct.bob.collections;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.api.APIEventsResponse;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.interfaces.APIFetchListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Place;
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
 * Nick on 28/10/2015.
 */
public class Events {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void fetchData(final Context context, final APIFetchListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        listener.startLoading();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.i("Events", "Call failed");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.failedLoading(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("Events", responseString);
                APIEventsResponse apiResponse = new Gson().fromJson(responseString, APIEventsResponse.class);
                Log.i("Events", apiResponse.data.events.toString());

                context.getContentResolver().bulkInsert(
                        PlaceEntry.CONTENT_URI,
                        Place.asContentValues(apiResponse.data.events));

                context.getContentResolver().bulkInsert(
                        EventEntry.CONTENT_URI,
                        Event.asContentValues(apiResponse.data.events));
            }
        });
    }

}
