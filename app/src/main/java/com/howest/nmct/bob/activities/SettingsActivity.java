package com.howest.nmct.bob.activities;

import com.howest.nmct.bob.fragments.SettingsFragment;

/**
 * illyism
 * 20/12/15
 */
public class SettingsActivity extends BaseActivity {
    SettingsFragment mFragment;

    @Override
    protected void initFragment() {
        if (mFragment == null) {
            mFragment = new SettingsFragment();
            addFragmentToContainer(mFragment);
        }
    }
}
