package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Callback;

import java.util.List;

import static com.howest.nmct.bob.Constants.EVENT;

/**
 * illyism
 * 24/11/15
 */
public class EventDetailsActivity extends BaseActivity implements Callback {
    private Event mEvent;
    private EventDetailsFragment mFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setStatusBarTranslucent(true);
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
        if (mFragment == null)
            mFragment = EventDetailsFragment.newInstance(mEvent);
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarImage(mEvent.getEventImage(), this);
        setToolbarTitle(mEvent.getEventName());
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
}
