package com.howest.nmct.bob.collections;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.api.APIEventsResponse;
import com.howest.nmct.bob.models.Event;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * Nick on 28/10/2015.
 */
public class Events {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static ArrayList<Event> events = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
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

    public static void addEvent(Event event) {
        events.add(event);
    }

    public static void addEvents(Event... events) {
        for (Event event : events) {
            addEvent(event);
        }
    }

    public static void addEvents(List<Event> events) {
        Events.events.addAll(events);
    }

    public static void fetchData(final Activity activity) {
        if (events.size() != 0) return;
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
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
            public void onFailure(Request request, IOException e) {
                Log.i("Events", "Call failed");
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
                        ((EventsLoadedListener) activity).eventsLoaded(events);
                    }
                });
            }
        });
    }



    public interface EventsLoadedListener {
        void eventsLoaded(ArrayList<Event> events);
    }
}
