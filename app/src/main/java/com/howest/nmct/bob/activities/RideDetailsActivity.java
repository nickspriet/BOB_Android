package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.models.Ride;

import java.util.List;

import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public class RideDetailsActivity extends BaseActivity {
    private Ride mRide;
    private RideDetailsFragment mFragment;

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
        if (frags != null)
            mFragment = (RideDetailsFragment) frags.get(0);
        if (mFragment == null)
            mFragment = RideDetailsFragment.newInstance(mRide);
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarImage(mRide.getImage());
        setToolbarTitle(mRide.getTitle());
        setHomeAsUp();
    }
}
