package com.howest.nmct.bob.activities;

import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.howest.nmct.bob.data.Contracts;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.sync.BackendSyncAdapter;

import java.util.List;

import static com.howest.nmct.bob.Constants.USER_ID;


/**
 * illyism
 * 24/11/15
 */
public class RidesActivity extends BaseActivity {
    private RidesFragment mFragment;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    ContentObserver userObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString(USER_ID, "");
        userObserver = new UserObserver(mainHandler);
        getContentResolver().registerContentObserver(Contracts.UserEntry.buildUserUri(userId), false, userObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(userObserver);
    }

    protected void initData() {
        BackendSyncAdapter.syncImmediately(this);
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

    class UserObserver extends ContentObserver {
        public UserObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mFragment.mAdapter.resetSwipeStates();
            mFragment.mAdapter.notifyDataSetChanged();
        }
    }
}
