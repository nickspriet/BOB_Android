package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.fragments.FeedFragment;

import java.util.List;

/**
 * illyism
 * 24/11/15
 */
public class FeedActivity extends BaseActivity {
    private FeedFragment mFragment;

    @Override
    protected void initData(Bundle activityData) {

    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (FeedFragment) frags.get(0);
        if (mFragment == null)
            mFragment = new FeedFragment();
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarTitle("Events");
    }
}
