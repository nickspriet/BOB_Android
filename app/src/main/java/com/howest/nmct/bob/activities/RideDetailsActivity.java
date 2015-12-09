package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;
import com.squareup.picasso.Callback;

import java.util.List;

import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public class RideDetailsActivity extends BaseActivity implements Callback {
    private Ride mRide;
    private RideDetailsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.event) {
            Event selectedEvent = Events.getEvent(mRide.event.getId());
            if (selectedEvent == null) selectedEvent = mRide.event;
            navigateToEventDetails(selectedEvent, (ImageView) findViewById(R.id.toolbarImage));
            return true;
        } else {
            throw new Error(String.format("Options Item not specified: %s", item));
        }
    }
}
