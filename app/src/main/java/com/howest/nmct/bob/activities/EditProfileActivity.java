package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.fragments.EditProfileFragment;
import com.howest.nmct.bob.models.User;

import java.util.List;

import static com.howest.nmct.bob.Constants.RESULTS_CLOSE;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.USER_ID;

/**
 * illyism
 * 09/12/15
 */
public class EditProfileActivity extends BaseActivity {
    private EditProfileFragment mFragment;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    ContentObserver userObserver;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHomeAsUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString(USER_ID, "");
        userObserver = new UserObserver(mainHandler);
        getContentResolver().registerContentObserver(UserEntry.buildUserUri(userId), false, userObserver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(userObserver);
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (EditProfileFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = new EditProfileFragment();
            addFragmentToContainer(mFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.close) {
            setResult(RESULTS_CLOSE);
            finish();
            return true;
        } else if (id == R.id.save) {
            Intent i = new Intent();
            User newUser = mFragment.getUser();
            getContentResolver().update(
                    UserEntry.buildUserUri(newUser.getId()),
                    User.asContentValues(newUser),
                    null,
                    null
            );
            setResult(RESULTS_OK, i);
            finish();
            return true;
        } else {
            throw new Error(String.format("Options Item not specified: %s", item));
        }
    }

    class UserObserver extends ContentObserver {
        public UserObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mFragment.initViews();
        }
    }
}
