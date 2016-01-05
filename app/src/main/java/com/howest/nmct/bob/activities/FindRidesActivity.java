package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.howest.nmct.bob.fragments.FindRidesFragment;

import java.util.List;

import static com.howest.nmct.bob.Constants.EVENT;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.RIDE;


/**
 * illyism
 * 24/11/15
 */
public class FindRidesActivity extends BaseActivity {
    private FindRidesFragment mFragment;
    private String mEventId;

    protected void setEventId(String eventId) {
        mEventId = eventId;
        if (mEventId == null || mEventId.isEmpty()) throw new Error("No Event ID in FindRidesActivity");
        Log.i("FindRidesActivity", "Loaded Event " + mEventId);
    }

    protected void initData(Bundle activityData) {
        if (activityData == null) return;
        setEventId(activityData.getString(EVENT));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EVENT, mEventId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData(getIntent() != null ? getIntent().getExtras() : savedInstanceState);
        super.onCreate(savedInstanceState);
        setHomeAsUp();
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (FindRidesFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = FindRidesFragment.newInstance(mEventId);
            addFragmentToContainer(mFragment);
        }
    }

    public void onRideSelected(String id) {
        Intent i = new Intent();
        i.putExtra(RIDE, id);
        setResult(RESULTS_OK, i);
        finish();
    }
}
