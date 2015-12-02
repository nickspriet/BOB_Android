package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.ProfileFragment;

import java.util.List;

/**
 * illyism
 * 24/11/15
 */
public class ProfileActivity extends BaseActivity {
    private ProfileFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    @Override
    protected void initData(Bundle activityData) {

    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (ProfileFragment) frags.get(0);
        if (mFragment == null)
            mFragment = new ProfileFragment();
        addFragmentToContainer(mFragment);
    }

    @Override
    protected void setupToolbar() {
        setToolbarImage(getUser().getCover());
        setToolbarTitle(getUser().getName());
    }
}
