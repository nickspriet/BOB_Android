package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.EditProfileFragment;
import com.howest.nmct.bob.models.User;

import java.util.List;

import static com.howest.nmct.bob.Constants.RESULTS_CLOSE;
import static com.howest.nmct.bob.Constants.RESULTS_OK;
import static com.howest.nmct.bob.Constants.RESULT_USER;

/**
 * illyism
 * 09/12/15
 */
public class EditProfileActivity extends BaseActivity {
    private EditProfileFragment mFragment;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle activityData) {}

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle(getUser().getName());
        setHomeAsUp();
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
            i.putExtra(RESULT_USER, newUser);
            setResult(RESULTS_OK, i);
            finish();
            return true;
        } else {
            throw new Error(String.format("Options Item not specified: %s", item));
        }
    }
}
