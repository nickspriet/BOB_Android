package com.howest.nmct.bob;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.fragments.FeedFragment;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Profile;
import com.howest.nmct.bob.models.Ride;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.nav_view) NavigationView navigationView;

    public Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProfile = new Profile("1", "Ilias Ismanalijev");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initDrawer();
        navigateToFeed();
    }

    /**
     * Sets up the Drawer Layout and a toggle to open the navigation menu.
     */
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
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

        return true;
    }

    public void navigateToProfile() {
        navigateToFragment(new ProfileFragment());
        toolbar.setTitle("Profile");
    }

    public void navigateToRides() {
        navigateToFragment(new RidesFragment());
        toolbar.setTitle("Rides");
    }

    public void navigateToEvents() {
        navigateToFragment(new EventsFragment());
        toolbar.setTitle("Events");
    }

    public void navigateToFeed() {
        navigateToFragment(new FeedFragment());
        toolbar.setTitle("Feed");
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
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.container, fragment)
                .addToBackStack(fragment.getClass().toString())
                .commit();
    }

    public void navigatetoRideDetails(Ride ride) {
        navigateToFragment(RideDetailsFragment.newInstance(ride), true);
        toolbar.setTitle(ride.getTitle());
    }

    public void navigatetoRideDetails(int frameLayout, Ride ride) {
        Fragment fragment = RideDetailsFragment.newInstance(ride);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout, fragment)
                .addToBackStack(fragment.getClass().toString())
                .commit();

    }
}
