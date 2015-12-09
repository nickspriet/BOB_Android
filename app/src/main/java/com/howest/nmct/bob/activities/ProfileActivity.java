package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.utils.IntentStarter;

import java.util.List;

import static com.howest.nmct.bob.Constants.REQUEST_EDIT;
import static com.howest.nmct.bob.Constants.RESULTS_CLOSE;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.RESULT_USER;

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
    protected void initData(Bundle activityData) {}

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
    protected void onStart() {
        super.onStart();
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
        if (requestCode == REQUEST_EDIT ) {
            switch (resultCode) {
                case RESULTS_CLOSE:
                    Toast.makeText(this, "Edit cancelled", Toast.LENGTH_SHORT).show();
                    break;
                case RESULTS_OK:
                    Toast.makeText(this, "Edit Saved", Toast.LENGTH_LONG).show();
                    User user = data.getExtras().getParcelable(RESULT_USER);
                    setUser(user);
                    mFragment.initViews();
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
