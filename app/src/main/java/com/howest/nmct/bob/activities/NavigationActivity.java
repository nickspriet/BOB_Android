package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.data.Contracts;
import com.howest.nmct.bob.data.DatabaseHelper;

import butterknife.Bind;

import static com.howest.nmct.bob.Constants.ACTIVITY_EVENTS;
import static com.howest.nmct.bob.Constants.ACTIVITY_FEED;
import static com.howest.nmct.bob.Constants.ACTIVITY_PROFILE;
import static com.howest.nmct.bob.Constants.ACTIVITY_RIDES;
import static com.howest.nmct.bob.Constants.ACTIVITY_SETTINGS;
import static com.howest.nmct.bob.Constants.APPBAR_TRANSITION_NAME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;
import static com.howest.nmct.bob.Constants.EVENT;
import static com.howest.nmct.bob.Constants.REQUEST_EDIT;
import static com.howest.nmct.bob.Constants.REQUEST_RIDE;
import static com.howest.nmct.bob.Constants.RIDE;
import static com.howest.nmct.bob.Constants.TOOLBAR_TRANSITION_NAME;
import static com.howest.nmct.bob.Constants.USER_ID;

/**
 * illyism
 * 24/11/15
 */
public abstract class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_MENU_ITEM_ID = "selectedMenuItemId";

    @Nullable @Bind(R.id.toolbarLayout) CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.appbarLayout) AppBarLayout appBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Nullable @Bind(R.id.toolbarImage) ImageView mToolbarImage;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedMenuItemId;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SELECTED_MENU_ITEM_ID, mSelectedMenuItemId);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Initializes the navigation drawer, the toggle button and toolbar
     */
    void initNavigation() {
        setSupportActionBar(mToolbar);
        initDrawerToggle();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Adds the home/up icon and adds the events
     * Links the icon to the drawerlayout and places it inside the toolbar
     */
    private void initDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        mSelectedMenuItemId = item.getItemId();
        updateNavigation(mSelectedMenuItemId);

        return true;
    }

    /**
     * Navigates to a fragment from a navigation drawer click
     *
     * @param itemId the id of the menu item
     */
    private void updateNavigation(int itemId) {
        switch (itemId) {
            case R.id.nav_feed:
                navigateToFeed();
                break;
            case R.id.nav_events:
                navigateToEvents();
                break;
            case R.id.nav_rides:
                navigateToRides();
                break;
            case R.id.nav_profile:
                navigateToProfile();
                break;
            case R.id.nav_settings:
                navigateToSettings();
                break;
            default:
                throw new Error(String.format("Navigation Item not specified: %s", itemId));
        }
    }

    public void navigateToProfile() {
        navigateToActivity(ACTIVITY_PROFILE);
    }

    public void navigateToSettings() {
        navigateToActivity(ACTIVITY_SETTINGS);
    }

    public void navigateToRides() {
        navigateToActivity(ACTIVITY_RIDES);
    }

    public void navigateToEvents() {
        navigateToActivity(ACTIVITY_EVENTS);
    }

    public void navigateToFeed() {
        navigateToActivity(ACTIVITY_FEED);
    }

    /**
     * Navigates to an activity
     *
     * @param activityName The name of the activity
     */
    private void navigateToActivity(String activityName) {
        Intent i;
        switch (activityName) {
            case ACTIVITY_PROFILE:
                i = new Intent(this, ProfileActivity.class);
                break;
            case ACTIVITY_FEED:
                i = new Intent(this, FeedActivity.class);
                break;
            case ACTIVITY_EVENTS:
                i = new Intent(this, EventsActivity.class);
                break;
            case ACTIVITY_RIDES:
                i = new Intent(this, RidesActivity.class);
                break;
            case ACTIVITY_SETTINGS:
                i = new Intent(this, SettingsActivity.class);
                break;
            default:
                throw new Error("Invalid activityName");
        }

        startActivity(i);
        finish();
    }

    public void navigateToActivity(Intent intent) {
        Pair appbar = new Pair<>(appBarLayout, APPBAR_TRANSITION_NAME);
        Pair toolbar = new Pair<>(mToolbar, TOOLBAR_TRANSITION_NAME);


        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, appbar, toolbar);

        ActivityCompat.startActivity(this,
                intent, transitionActivityOptions.toBundle());
    }

    public void navigateToActivityForResult(Intent intent, int requestCode) {
        Pair appbar = new Pair<>(appBarLayout, APPBAR_TRANSITION_NAME);
        Pair toolbar = new Pair<>(mToolbar, TOOLBAR_TRANSITION_NAME);

        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, appbar, toolbar);

        ActivityCompat.startActivityForResult(this, intent, requestCode, transitionActivityOptions.toBundle());
    }

    /**
     * Shows the back icon
     */
    void setHomeAsUp() {
        if (getSupportActionBar() != null) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Starts the RideDetailsActivity
     * @param rideId The Ride ID
     */
    public void navigateToRideDetails(String rideId) {
        Intent i = new Intent(this, RideDetailsActivity.class);
        i.putExtra(RIDE, rideId);
        navigateToActivity(i);
    }

    /**
     * Starts the EventDetailsActivity
     * @param eventId The Event ID
     */
    public void navigateToEventDetails(String eventId) {
        Intent i = new Intent(this, EventDetailsActivity.class);
        i.putExtra(EVENT, eventId);
        navigateToActivity(i);
    }

    /**
     * Starts the EditProfileActivity
     */
    public void navigateToEditProfile() {
        Intent i = new Intent(this, EditProfileActivity.class);
        navigateToActivityForResult(i, REQUEST_EDIT);
    }

    public void navigateToFindRides(String eventId) {
        Intent i = new Intent(this, FindRidesActivity.class);
        i.putExtra(EVENT, eventId);
        navigateToActivityForResult(i, REQUEST_RIDE);
    }

    /**
     * Clears all saved user data
     * Navigate to Login
     */
    public void navigatetoLogin() {
        Log.d("NavigationActivity", "Navigating to login - killing user dat");
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove(BACKEND_TOKEN).remove(USER_ID).apply();

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        db.delete(Contracts.UserEntry.TABLE_NAME, null, null);
        db.delete(Contracts.PlaceEntry.TABLE_NAME, null, null);
        db.delete(Contracts.EventEntry.TABLE_NAME, null, null);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
