package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.utils.IntentStarter;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.edit) {
            return true;
        } else if (id == R.id.facebook) {
            IntentStarter.openFacebookProfile(this, getUser().getId());
            return true;
        } else {
            throw new Error(String.format("Options Item not specified: %s", item));
        }
    }


}
