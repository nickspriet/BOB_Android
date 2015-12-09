package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.interfaces.EventsLoadedListener;
import com.howest.nmct.bob.models.Event;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * illyism
 * 24/11/15
 */
public class EventsActivity extends BaseActivity implements EventsLoadedListener {
    private EventsFragment mFragment;

    @Override
    protected void initData(Bundle activityData) {
        Events.fetchData(this);
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

    @Override
    public void eventsLoaded(LinkedHashSet<Event> events) {
        mFragment.mAdapter.setEvents(events);
        mFragment.mAdapter.notifyDataSetChanged();
    }
}
