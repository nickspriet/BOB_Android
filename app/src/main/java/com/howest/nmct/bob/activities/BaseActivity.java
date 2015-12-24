package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.interfaces.ToolbarController;
import com.howest.nmct.bob.models.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.ButterKnife;

import static com.howest.nmct.bob.Constants.USER_ID;
import static com.howest.nmct.bob.Constants.USER_PROFILE;

/**
 * A BaseActivity to inherit from
 * - Holds User object
 * - Controls the toolbar images and title with ToolbarController
 * - Contains one fragment
 */
public abstract class BaseActivity extends NavigationActivity implements ToolbarController,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int USER_LOADER = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayout());
        ButterKnife.bind(this);

        initUserData(savedInstanceState);
        super.initNavigation();
        initFragment();
    }

    @SuppressWarnings("SameReturnValue")
    protected int getContentLayout() {
        return R.layout.activity_main;
    }
    protected abstract void initFragment();

    private User mUser;
    public User getUser() {
        return mUser;
    }

    /**
     * Set new values for the user, only allowed if it is the same user
     * @param user the User with new values
     * @return User the current User
     */
    protected User setUser(User user) {
        if (mUser == null || mUser.getId().equals(user.getId())) {
            mUser = user;
            getContentResolver().notifyChange(UserEntry.buildUserUri(mUser.getId()), null);
        }
        return mUser;
    }

    private void initUserData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(USER_PROFILE);
            if (mUser != null) initDrawerHeader();
        }
        if (mUser == null) {
            getSupportLoaderManager().initLoader(USER_LOADER, null, this);
        }
    }

    public void reloadUser() {
        getSupportLoaderManager().restartLoader(USER_LOADER, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_PROFILE, mUser);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUser = savedInstanceState.getParcelable(USER_PROFILE);
    }

    /**
     * Sets the navigation drawer header for the logged in profile
     */
    private void initDrawerHeader() {
        // Set profile
        Picasso picasso = Picasso.with(this);
        View headerView = mNavigationView.getHeaderView(0);

        ImageView ivNavHeaderProfile = (ImageView) headerView.findViewById(R.id.nav_header_profile);
        ImageView ivNavHeaderBackground = (ImageView) headerView.findViewById(R.id.nav_header_background);

        if (!mUser.getPicture().isEmpty() && ivNavHeaderProfile != null) {
            picasso.load(mUser.getPicture())
                    .fit()
                    .centerCrop()
                    .into(ivNavHeaderProfile);
        }

        if (!mUser.getCover().isEmpty() && ivNavHeaderBackground != null) {
            picasso.load(mUser.getCover())
                    .fit()
                    .centerCrop()
                    .into(ivNavHeaderBackground);
        }


        TextView tvName = (TextView) headerView.findViewById(R.id.nav_header_name);
        if (tvName != null) tvName.setText(mUser.getName());
    }

    @Override
    public void setToolbarImage(String url) {
        if (mToolbarImage != null) {
            Picasso p = Picasso.with(this);
            p.load(url)
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(mToolbarImage);
        }
    }

    @Override
    public void setToolbarImage(String url, Callback callback) {
        if (mToolbarImage != null) {
            Picasso p = Picasso.with(this);
            p.load(url)
                    .noFade()
                    .fit()
                    .centerCrop()
                    .into(mToolbarImage, callback);
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        if (mToolbarLayout == null) return;
        mToolbarLayout.setTitle(title);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Adds a fragment to the container framelayout
     * @param fragment A fragment that will be shown
     */
    void addFragmentToContainer(Fragment fragment) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Adds a fragment to the container framelayout
     * @param fragment A fragment that will be shown
     */
    void addFragmentToContainer(android.app.Fragment fragment) {
        getFragmentManager().popBackStack();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void addDataToIntent(Intent i) {
        i.putExtra(USER_PROFILE, mUser);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case USER_LOADER:
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String userId = preferences.getString(USER_ID, "");
                return new CursorLoader(this, UserEntry.buildUserUri(userId), null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            Log.e("BaseActivity", "No user found " + data.getColumnCount() + " "
                    + Arrays.toString(data.getColumnNames()));
            data.close();
            navigatetoLogin();
            return;
        }
        setUser(User.createFromCursor(data));
        data.close();
        initDrawerHeader();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
