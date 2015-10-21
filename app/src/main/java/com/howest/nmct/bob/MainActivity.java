package com.howest.nmct.bob;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.fragments.FeedFragment;
import com.howest.nmct.bob.fragments.ProfileFragment;
import com.howest.nmct.bob.fragments.RidesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout container;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawer();
        initContent();
    }

    private void initContent() {
        container = (FrameLayout) findViewById(R.id.container);
        navigateToRides();
    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (id) {
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

    private void navigateToProfile() {
        navigateToFragment(new ProfileFragment());
        toolbar.setTitle("Profile");
    }

    private void navigateToRides() {
        navigateToFragment(new RidesFragment());
        toolbar.setTitle("Rides");
    }

    private void navigateToEvents() {
        navigateToFragment(new EventsFragment());
        toolbar.setTitle("Events");
    }

    private void navigateToFeed() {
        navigateToFragment(new FeedFragment());
        toolbar.setTitle("Feed");
    }

    private void navigateToFragment(Fragment fragment) {
        if (container == null) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
