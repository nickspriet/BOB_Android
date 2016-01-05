package com.howest.nmct.bob.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.util.Log;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.interfaces.EventActionsListener;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.sync.BackendSyncAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.howest.nmct.bob.Constants.EVENT;
import static com.howest.nmct.bob.Constants.REQUEST_RIDE;
import static com.howest.nmct.bob.Constants.RESULTS_CLOSE;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public class EventDetailsActivity extends BaseActivity implements
    EventActionsListener {

    private String mEventId;
    private EventDetailsFragment mFragment;

    private String mTitle;

    protected void onCreate(Bundle savedInstanceState) {
        if (!parseIntent()) {
            initData(getIntent() != null ? getIntent().getExtras() : savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);

        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}
            @Override
            public void onTransitionEnd(Transition transition) {
                setToolbarTitle(mTitle);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
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
        setEventId(activityData.getString(EVENT));
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
        this.mTitle = title;
        setToolbarImage(cover);
        setToolbarTitle(mTitle);
    }

    @Override
    public void onShareRide() {
        final Context activityContext = this;

        Cursor c = getContentResolver().query(RideEntry.CONTENT_URI,
                new String[] { RideEntry.TABLE_NAME + "." + RideEntry._ID },
                RideEntry.COLUMN_EVENT_ID + "=?",
                new String[] {mEventId},
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            // Event already exists
            navigateToRideDetails(c.getString(0));
            c.close();
        } else {
            BackendSyncAdapter.createRideFromEvent(this, mEventId, new ResponseListener() {
                @Override
                public void onSuccess(String id) {
                    Log.i("EventDetailsActivity", "Ride is created - " + id);
                    navigateToRideDetails(id);
                    Toast.makeText(activityContext, "Ride created", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("EventDetailsActivity", "Failed to save ride", e);
                    Toast.makeText(activityContext, "Failed to save this ride", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onHide(Boolean shouldHide) {
        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_HIDE, shouldHide ? Event.HIDDEN : Event.VISIBILE);
        getContentResolver().update(
                EventEntry.buildEventUri(mEventId),
                values, null, null
        );
        finish();
    }

    @Override
    public void onFindRide() {
        navigateToFindRides(mEventId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RIDE) {
            switch (resultCode) {
                case RESULTS_OK:
                    Toast.makeText(this, "Ride selected", Toast.LENGTH_SHORT).show();
                    String rideId = data.getExtras().getString(RIDE);
                    final Context context = this;
                    BackendSyncAdapter.requestRide(this, rideId, new ResponseListener() {
                        @Override
                        public void onSuccess(String id) {
                            navigateToRideDetails(id);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                case RESULTS_CLOSE:
                    Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
