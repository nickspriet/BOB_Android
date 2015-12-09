package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;

import butterknife.Bind;

import static com.howest.nmct.bob.Constants.ACTIVITY_EVENTS;
import static com.howest.nmct.bob.Constants.ACTIVITY_FEED;
import static com.howest.nmct.bob.Constants.ACTIVITY_PROFILE;
import static com.howest.nmct.bob.Constants.ACTIVITY_RIDES;
import static com.howest.nmct.bob.Constants.EVENT;
import static com.howest.nmct.bob.Constants.REQUEST_EDIT;
import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public abstract class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_MENU_ITEM_ID = "selectedMenuItemId";

    @Bind(R.id.toolbarLayout) CollapsingToolbarLayout mToolbarLayout;
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawer(GravityCompat.START);
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
     * @param itemId the id of the menu item
     */
    private void updateNavigation(int itemId) {
        switch (itemId) {
            case R.id.nav_feed:
                Log.d("NavigationDrawer", "Click Feed");
                navigateToFeed();
                break;
            case R.id.nav_events:
                Log.d("NavigationDrawer", "Click Events");
                navigateToEvents();
                break;
            case R.id.nav_rides:
                Log.d("NavigationDrawer", "Click Rides");
                navigateToRides();
                break;
            case R.id.nav_profile:
                Log.d("NavigationDrawer", "Click Profile");
                navigateToProfile();
                break;
            default:
                throw new Error(String.format("Navigation Item not specified: %s", itemId));
        }
    }

    public void navigateToProfile() {
        navigateToActivity(ACTIVITY_PROFILE);
        mToolbarLayout.setTitle("Profile");
    }

    public void navigateToRides() {
        navigateToActivity(ACTIVITY_RIDES);
        mToolbarLayout.setTitle("Rides");
    }

    public void navigateToEvents() {
        navigateToActivity(ACTIVITY_EVENTS);
        mToolbarLayout.setTitle("Events");
    }

    public void navigateToFeed() {
        navigateToActivity(ACTIVITY_FEED);
        mToolbarLayout.setTitle("Feed");
    }

    /**
     * Navigates to an activity
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
            default:
                throw new Error("Invalid activityName");
        }

        addDataToIntent(i);
        startActivity(i);
        finish();
    }

    /**
     * Adds data to the intent - Defined by inheritors
     * @param i the intent
     */
    protected abstract void addDataToIntent(Intent i);

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
     * Starts the RideActivity and inserts the Ride bundle
     * @param ride The Ride
     */
    public void navigateToRideDetails(Ride ride) {
        Intent i = new Intent(this, RideDetailsActivity.class);
        addDataToIntent(i);
        i.putExtra(RIDE, ride);
        Pair toolbar = new Pair<>(mToolbarLayout, "Toolbar");

        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, toolbar);

        ActivityCompat.startActivity(this,
                i, transitionActivityOptions.toBundle());
    }

    /**
     * Starts the RideActivity and inserts the Ride bundle with a transition
     * @param ride The Ride
     * @param imageView The View that is shared
     */
    public void navigateToRideDetails(Ride ride, ImageView imageView) {
        Intent i = new Intent(this, RideDetailsActivity.class);
        addDataToIntent(i);
        i.putExtra(RIDE, ride);

        Pair image = new Pair<>(imageView, ViewCompat.getTransitionName(imageView));
        Pair toolbar = new Pair<>(mToolbarLayout, "Toolbar");

        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, image, toolbar);

        ActivityCompat.startActivity(this,
                i, transitionActivityOptions.toBundle());
    }

    /**
     * Starts the EventDetailsActivity and inserts the Event bundle with a transition
     * @param event The Event
     * @param imageView The View that is shared
     */
    public void navigateToEventDetails(Event event, ImageView imageView) {
        Intent i = new Intent(this, EventDetailsActivity.class);
        addDataToIntent(i);
        i.putExtra(EVENT, event);

        Pair image = new Pair<>(imageView, ViewCompat.getTransitionName(imageView));
        Pair toolbar = new Pair<>(mToolbarLayout, "Toolbar");

        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, image, toolbar);

        ActivityCompat.startActivity(this,
                i, transitionActivityOptions.toBundle());
    }

    /**
     * Starts the EditProfileActivity
     */
    public void navigateToEditProfile() {
        Intent i = new Intent(this, EditProfileActivity.class);
        addDataToIntent(i);

        Pair toolbar = new Pair<>(mToolbarLayout, "Toolbar");

        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, toolbar);

        ActivityCompat.startActivityForResult(this, i, REQUEST_EDIT,
                transitionActivityOptions.toBundle());
    }

    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     *
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     *     lives inside a Fragment hosted by the called Activity).
     *
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     *     asynchronously load/scale a bitmap before the transition can begin).
     *
     * (3) Inside a LoaderCallback's onLoadFinished() method (if the shared
     *     element depends on data queried by a Loader).
     */
    protected void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }



}
