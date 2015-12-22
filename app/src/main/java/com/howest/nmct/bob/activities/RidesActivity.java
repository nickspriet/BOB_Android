package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.interfaces.RidesLoadedListener;
import com.howest.nmct.bob.models.Ride;

import java.util.LinkedHashSet;
import java.util.List;


/**
 * illyism
 * 24/11/15
 */
public class RidesActivity extends BaseActivity implements RidesLoadedListener {
    private RidesFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    protected void initData() {
        Rides.fetchData(this, this);
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (RidesFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = new RidesFragment();
            addFragmentToContainer(mFragment);
        }
    }

    @Override
    public void ridesLoaded(LinkedHashSet<Ride> rides) {
        mFragment.mAdapter.setRides(rides);
        mFragment.mAdapter.resetSwipeStates();
        mFragment.mAdapter.notifyDataSetChanged();
    }
}
