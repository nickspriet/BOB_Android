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
import com.howest.nmct.bob.interfaces.EventsLoadedListener;
import com.howest.nmct.bob.models.Event;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedHashSet;

import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * Nick on 28/10/2015.
 */
public class Events {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static LinkedHashSet<Event> events = new LinkedHashSet<>();

    public static LinkedHashSet<Event> getEvents() {
        return events;
    }

    public static Event getEvent(final String id) {
        Event foundEvent = null;

        for (Event e : events) {
            if (e.getId().equals(id)) {
                foundEvent = e;
                break;
            }
        }

        return foundEvent;
    }

    public static void addEvents(LinkedHashSet<Event> events) {
        Events.events.addAll(events);
    }

    public static void fetchData(Context context, final EventsLoadedListener listener) {
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
                addEvents(apiResponse.data.events);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.eventsLoaded(events);
                    }
                });
            }
        });
    }
}
