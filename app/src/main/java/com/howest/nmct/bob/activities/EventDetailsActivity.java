package com.howest.nmct.bob.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.fragments.CreateRideDialogFragment;
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.interfaces.ResponseListener;
import com.howest.nmct.bob.interfaces.RidesLoadedListener;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;
import com.squareup.picasso.Callback;

import java.util.LinkedHashSet;
import java.util.List;

import static com.howest.nmct.bob.Constants.EVENT;

/**
 * illyism
 * 24/11/15
 */
public class EventDetailsActivity extends BaseActivity implements Callback,
        CreateRideDialogFragment.RideOptionSelectedListener {
    private Event mEvent;
    private EventDetailsFragment mFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    @Override
    protected void initData(Bundle activityData) {
        mEvent = activityData.getParcelable(EVENT);
        if (mEvent == null) throw new Error("No Event in EventDetailsActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EVENT, mEvent);
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
            mFragment = EventDetailsFragment.newInstance(mEvent);
            addFragmentToContainer(mFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarImage(mEvent.getCover(), this);
        setToolbarTitle(mEvent.getName());
        setHomeAsUp();
    }

    /**
     * Picasso Callback
     */
    @Override
    public void onSuccess() {
        scheduleStartPostponedTransition(findViewById(R.id.toolbarImage));
    }

    /**
     * Picasso Callback
     */
    @Override
    public void onError() {
        scheduleStartPostponedTransition(findViewById(R.id.toolbarImage));
    }

    @Override
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

    @Override
    public void onDialogNotBobClick(Event mEvent) {

    }

    public void onGoing() {
        DialogFragment dialog = CreateRideDialogFragment.newInstance(this, mEvent);
        dialog.show(getSupportFragmentManager(), "CreateRideDialogFragment");
    }

    public void onInterested() {

    }

    public void onNotGoing() {

    }
}
