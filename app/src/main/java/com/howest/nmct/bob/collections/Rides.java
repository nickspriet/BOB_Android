package com.howest.nmct.bob.collections;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.howest.nmct.bob.api.APIRideResponse;
import com.howest.nmct.bob.data.Contracts;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.models.Ride;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.howest.nmct.bob.Constants.API_RIDE_CREATE;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * illyism
 * 22/10/15
 */
public class Rides {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void createRideFromEvent(final Context context, String eventId, final ResponseListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(BACKEND_TOKEN, "");

        RequestBody body = new FormEncodingBuilder()
                .add("token", token)
                .add("eventId", eventId)
                .build();

        Request request = new Request.Builder()
                .url(API_RIDE_CREATE)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e("Rides", "Call failed");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("Rides", responseString);
                final APIRideResponse apiResponse = new Gson().fromJson(responseString, APIRideResponse.class);

                context.getContentResolver().insert(Contracts.EventEntry.CONTENT_URI,
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

            }
        });
    }
}
