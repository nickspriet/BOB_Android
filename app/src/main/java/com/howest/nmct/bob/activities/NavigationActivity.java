package com.howest.nmct.bob.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.howest.nmct.bob.fragments.EventDetailsFragment;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.fragments.FeedFragment;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;

import butterknife.Bind;

/**
 * illyism
 * 24/11/15
 */
public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private static final String SELECTED_MENU_ITEM_ID = "selectedMenuItemId";

    @Bind(R.id.toolbarLayout) CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.toolbarImage) ImageView mToolbarImage;
    @Bind(R.id.toolbarOverlay) View mToolbarOverlay;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedMenuItemId;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SELECTED_MENU_ITEM_ID, mSelectedMenuItemId);
    }

    /**
     * Initializes the navigation drawer, the toggle button and toolbar
     */
    public void initNavigation() {
        initToolbar();
        initDrawerToggle();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Sets the toolbar as the actionbar
     */
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    /**
     * Sets the up or home icon depending on the backstack
     */
    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
        } else {
            setToolbarTitle(getSupportFragmentManager().getFragments().get(0).getClass().getSimpleName());
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        mSelectedMenuItemId = item.getItemId();
        updateNavigation(mSelectedMenuItemId);

        return true;
    }

    private void setToolbarTitle(String className) {
        String title = "BOB";
        switch (className) {
            case "EventsFragment":
                title = "Events";
                break;
            case "ProfileFragment":
                title = "Profile";
                break;
            case "FeedFragment":
                title = "Feed";
                break;
            case "RidesFragment":
                title = "Rides";
                break;
        }
        mToolbarLayout.setTitle(title);
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
        }
    }

    public void navigateToProfile() {
        navigateToFragment(new ProfileFragment());
        mToolbarLayout.setTitle("Profile");
    }

    public void navigateToRides() {
        navigateToFragment(new RidesFragment());
        mToolbarLayout.setTitle("Rides");
    }

    public void navigateToEvents() {
        navigateToFragment(new EventsFragment());
        mToolbarLayout.setTitle("Events");
    }

    public void navigateToFeed() {
        navigateToFragment(new FeedFragment());
        mToolbarLayout.setTitle("Feed");
    }

    /**
     * Navigates to a fragment and places it in the container.
     * @param fragment A created fragment that is navigated to
     */
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void navigateToFragment(Fragment fragment, Boolean addToManager) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().toString())
                .commit();
    }

    public void navigatetoRideDetails(Ride ride) {
        navigateToFragment(RideDetailsFragment.newInstance(ride), true);
    }

    public void navigateToEventDetails(Event event) {
        navigateToFragment(EventDetailsFragment.newInstance(event), true);
    }

}
