package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.fragments.EventsFragment;

import java.util.List;

/**
 * illyism
 * 24/11/15
 */
public class EventsActivity extends BaseActivity {
    private EventsFragment mFragment;

    @Override
    protected void initData(Bundle activityData) {
        if (Events.getEvents().size() == 0)
            Events.fetchData();
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (EventsFragment) frags.get(0);
        if (mFragment == null)
            mFragment = new EventsFragment();
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarTitle("Events");
    }

}
