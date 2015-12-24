package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.api.APIRideResponse;
import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Callback;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;
import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public class RideDetailsActivity extends BaseActivity implements Callback {
    private Ride mRide;
    private RideDetailsFragment mFragment;
    private ShareActionProvider mShareActionProvider;
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    // @TODO: Other way to fetch User
    private boolean parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();
        if (uri == null) return false;
        Log.d("URI", "Received data: " + uri);

        String path = uri.getPath();
        Log.d("URI", "Received path: " + path);
        Pattern pattern = Pattern.compile("^/ride/(.*)/?$");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return false;
        }

        String rideId = matcher.group(1);
        loadData(rideId);
        return true;
    }

    private void loadData(String rideId) {
        Log.i("RideDetailsActivity", "Loading ride " + rideId);
        OkHttpClient okHttpClient = new OkHttpClient();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("RideDetailsActivity", "Call failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("RideDetailsActivity", responseString);
                APIRideResponse apiResponse = new Gson().fromJson(responseString, APIRideResponse.class);
                Log.i("RideDetailsActivity", apiResponse.data.ride.toString());
                mRide = apiResponse.data.ride;
                mFragment.setRide(mRide);


                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFragment.initViews();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parseIntent();
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setStatusBarTranslucent(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarImage(mRide.event.getCover(), this);
        setToolbarTitle(mRide.event.getName());
        setHomeAsUp();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    protected void initData(Bundle activityData) {
        mRide = activityData.getParcelable(RIDE);
        if (mRide == null) throw new Error("No Ride in RideDetailsActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RIDE, mRide);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null) {
            mFragment = (RideDetailsFragment) frags.get(0);
            mFragment.setRide(mRide);
        }
        if (mFragment == null) {
            mFragment = RideDetailsFragment.newInstance(mRide);
            addFragmentToContainer(mFragment);
        }
    }


    /**
     * Picasso Callback
     */
    @Override
    public void onSuccess() {
        scheduleStartPostponedTransition(findViewById(R.id.toolbarImage));
        setToolbarTitle(mRide.event.getName());
    }

    /**
     * Picasso Callback
     */
    @Override
    public void onError() {
        scheduleStartPostponedTransition(findViewById(R.id.toolbarImage));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ride, menu);

        MenuItem item = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mRide.getLink());
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.event) {
            Event selectedEvent = Events.getEvent(mRide.event.getId());
            if (selectedEvent == null) selectedEvent = mRide.event;
            navigateToEventDetails(selectedEvent.getId(), (ImageView) findViewById(R.id.toolbarImage));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
