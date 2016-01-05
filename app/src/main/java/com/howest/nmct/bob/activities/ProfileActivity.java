package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.utils.IntentStarter;

import java.util.List;

import static com.howest.nmct.bob.Constants.REQUEST_EDIT;
import static com.howest.nmct.bob.Constants.RESULTS_CLOSE;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.USER_ID;

/**
 * illyism
 * 24/11/15
 */
public class ProfileActivity extends BaseActivity {
    private ProfileFragment mFragment;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    ContentObserver userObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString(USER_ID, "");
        userObserver = new UserObserver(mainHandler);
        getContentResolver().registerContentObserver(UserEntry.buildUserUri(userId), false, userObserver);
    }

    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(userObserver);
        super.onDestroy();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (ProfileFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = new ProfileFragment();
            addFragmentToContainer(mFragment);
        }
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
            navigateToEditProfile();
            return true;
        } else if (id == R.id.facebook) {
            IntentStarter.openFacebookProfile(this, getUser());
            return true;
        } else {
            throw new Error(String.format("Options Item not specified: %s", item));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FixMe: Losing focus on window
        if (requestCode == REQUEST_EDIT ) {
            switch (resultCode) {
                case RESULTS_CLOSE:
                    Toast.makeText(this, "Edit cancelled", Toast.LENGTH_SHORT).show();
                    navigateToProfile();
                    finish();
                    break;
                case RESULTS_OK:
                    Toast.makeText(this, "Edit Saved", Toast.LENGTH_LONG).show();
                    // reloadUser();
                    navigateToProfile();
                    finish();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    class UserObserver extends ContentObserver {
        public UserObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            User u = getUser();
            setToolbarImage(u.getCover());
            setToolbarTitle(u.getName());
            mFragment.initViews();
        }
    }
}
