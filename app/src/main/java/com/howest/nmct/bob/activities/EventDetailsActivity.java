package com.howest.nmct.bob.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.fragments.CreateRideDialogFragment;
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.interfaces.EventActionsListener;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.interfaces.RideOptionSelectedListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.howest.nmct.bob.Constants.EVENT;

/**
 * illyism
 * 24/11/15
 */
public class EventDetailsActivity extends BaseActivity implements RideOptionSelectedListener,
    EventActionsListener {

    private String mEventId;
    private EventDetailsFragment mFragment;

    private static final int URL_LOADER = 0;

    protected void onCreate(Bundle savedInstanceState) {
        if (!parseIntent()) {
            initData(getIntent() != null ? getIntent().getExtras() : savedInstanceState);
        }
        initData(getIntent() != null ?   getIntent().getExtras() : savedInstanceState);
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setHomeAsUp();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    /**
     * Get the id from the Intent URI
     *
     * @return bool if an id was found
     */
    private boolean parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();
        if (uri == null) return false;
        Log.d("URI", "Received data: " + uri);

        String path = uri.getPath();
        Log.d("URI", "Received path: " + path);
        Pattern pattern = Pattern.compile("^/event/(.*)/?$");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return false;
        }

        String eventId = matcher.group(1);
        setEventId(eventId);
        return true;
    }

    protected void setEventId(String eventId) {
        mEventId = eventId;
        if (mEventId == null || mEventId.isEmpty()) throw new Error("No Event ID in EventDetailsActivity");
        Log.i("EventDetailsActivity", "Loaded Event " + mEventId);
    }

    protected void initData(Bundle activityData) {
        if (activityData == null) return;
        String oldEventId = mEventId;
        mEventId = activityData.getString(EVENT);
        if (mEventId == null || mEventId.isEmpty()) throw new Error("No Event ID in EventDetailsActivity");
        Log.i("EventDetailsActivity", "Loaded Event " + mEventId);
        if (!mEventId.equals(oldEventId))
            getSupportLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EVENT, mEventId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (EventDetailsFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = EventDetailsFragment.newInstance(mEventId);
            addFragmentToContainer(mFragment);
        }
    }

    public void initToolbar(String cover, String title) {
        setToolbarImage(cover);
        setToolbarTitle(title);
    }

    public void onDialogBobClick(final String eventId) {
        final Context activityContext = this;
        Rides.createRideFromEvent(this, mEventId, new ResponseListener() {
            @Override
            public void onSuccess() {
                Log.i("EventDetailsActivity", "Ride is created");
                Rides.fetchData(activityContext, null);
                Toast.makeText(activityContext, "Ride created", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure() {
                Log.e("EventDetailsActivity", "Failed to save ride");
                Toast.makeText(activityContext, "Failed to save this ride", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onDialogNotBobClick(String mEventId) {}

    public void onGoing() {
        DialogFragment dialog = CreateRideDialogFragment.newInstance(this, mEventId);
        dialog.show(getSupportFragmentManager(), "CreateRideDialogFragment");
    }

    public void onInterested() {}
    public void onNotGoing() {}
}
