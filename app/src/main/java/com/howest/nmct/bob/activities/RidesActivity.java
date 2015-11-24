package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.fragments.RidesFragment;

import java.util.List;

/**
 * illyism
 * 24/11/15
 */
public class RidesActivity extends BaseActivity {
    public RidesFragment mFragment;

    @Override
    protected void initData(Bundle activityData) {
        if (Rides.getRides().size() == 0)
            Rides.fetchData();
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (RidesFragment) frags.get(0);
        if (mFragment == null)
            mFragment = new RidesFragment();
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarTitle("Rides");
    }
}
