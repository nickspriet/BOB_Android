package com.howest.nmct.bob.collections;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.api.APIResponse;
import com.howest.nmct.bob.api.APIRidesResponse;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.interfaces.RidesLoadedListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedHashSet;

import static com.howest.nmct.bob.Constants.API_RIDE_CREATE;
import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * illyism
 * 22/10/15
 */
public class Rides {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static LinkedHashSet<Ride> rides = new LinkedHashSet<>();

    public static LinkedHashSet<Ride> getRides() {
        return rides;
    }

    private static void addRides(LinkedHashSet<Ride> rides) {
        Rides.rides.addAll(rides);
    }


    public static void fetchData(final Context context, final RidesLoadedListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
            public void onFailure(Request request, IOException e) {
                Log.i("Rides", "Call failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("Rides", responseString);
                APIRidesResponse apiResponse = new Gson().fromJson(responseString, APIRidesResponse.class);
                Log.i("Rides", apiResponse.data.rides.toString());
                addRides(apiResponse.data.rides);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.ridesLoaded(rides);
                    }
                });
            }
        });
    }

    public static void createRideFromEvent(final Context context, Event event, final ResponseListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = new FormEncodingBuilder()
                .add("token", token)
                .add("eventId", event.getId())
                .build();

        Request request = new Request.Builder()
                .url(API_RIDE_CREATE)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("LoginActivity", "Call failed");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("LoginActivity", responseString);
                final APIResponse apiResponse = new Gson().fromJson(responseString, APIResponse.class);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (apiResponse.statusCode == 200) {
                            listener.onSuccess();
                        } else {
                            listener.onFailure();
                        }
                    }
                });

            }
        });
    }

    public static Ride getRideForEvent(Event event) {
        for (Ride ride : rides) {
            if (ride.event.getId().equals(event.getId())) {
                return ride;
            }
        }
        return null;
    }
}
