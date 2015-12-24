package com.howest.nmct.bob.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.fragments.CreateRideDialogFragment;
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.interfaces.EventActionsListener;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.interfaces.RideOptionSelectedListener;
import com.howest.nmct.bob.interfaces.RidesLoadedListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;

import java.util.LinkedHashSet;
import java.util.List;

import static com.howest.nmct.bob.Constants.EVENT;

/**
 * illyism
 * 24/11/15
 */
public class EventDetailsActivity extends BaseActivity implements RideOptionSelectedListener,
        EventActionsListener, LoaderManager.LoaderCallbacks<Cursor> {

    private String mEventId;
    private Event mEvent;
    private EventDetailsFragment mFragment;

    private static final int URL_LOADER = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(getIntent() != null ? getIntent().getExtras() : savedInstanceState);
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
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
            mFragment = EventDetailsFragment.newInstance(null);
            addFragmentToContainer(mFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHomeAsUp();
    }

    private void initToolbar() {
        setToolbarImage(mEvent.getCover());
        setToolbarTitle(mEvent.getName());
    }


    public void onDialogBobClick(final Event event) {
        final Context activityContext = this;
        Rides.createRideFromEvent(this, event, new ResponseListener() {
            @Override
            public void onSuccess() {
                Log.i("EventDetailsActivity", "Ride is created");
                Rides.fetchData(activityContext, new RidesLoadedListener() {
                    @Override
                    public void ridesLoaded(LinkedHashSet<Ride> rides) {
                        Log.i("EventDetailsActivity", "New Rides are loaded");
                        navigateToRideDetails(Rides.getRideForEvent(event));
                        finish();
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.e("EventDetailsActivity", "Failed to save ride");
                Toast.makeText(activityContext, "Failed to save this ride", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onDialogNotBobClick(Event mEvent) {}

    public void onGoing() {
        DialogFragment dialog = CreateRideDialogFragment.newInstance(this, mEvent);
        dialog.show(getSupportFragmentManager(), "CreateRideDialogFragment");
    }

    public void onInterested() {}
    public void onNotGoing() {}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        this,
                        EventEntry.buildEventUri(mEventId),
                        null,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("EventDetailsActivity", "Finished loading event " + data.getCount());
        if (data.moveToFirst()) {
            mEvent = Event.createFromCursor(data);
            mFragment.setEvent(mEvent);
            initToolbar();
        } else {
            Toast.makeText(this, "No event found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
