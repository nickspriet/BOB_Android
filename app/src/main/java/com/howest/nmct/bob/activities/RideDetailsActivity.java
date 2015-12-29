package com.howest.nmct.bob.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.RideDetailsFragment;
import com.howest.nmct.bob.models.Ride;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.howest.nmct.bob.Constants.RIDE;

/**
 * illyism
 * 24/11/15
 */
public class RideDetailsActivity extends BaseActivity {

    private String mRideId;
    private RideDetailsFragment mFragment;

    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!parseIntent()) {
            initData(getIntent() != null ? getIntent().getExtras() : savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);

        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}
            @Override
            public void onTransitionEnd(Transition transition) {
                setToolbarTitle(mTitle);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHomeAsUp();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_expanded;
    }

    /**
     * Get the id from the Intent URI
     * @return bool if an id was found
     */
    private boolean parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();
        if (uri == null) return false;
        Log.d("URI", "Received data: " + uri);

        String path = uri.getPath();
        Log.d("URI", "Received path: " + path);
        Pattern pattern = Pattern.compile("^/ride/(.*)/?$");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return false;
        }

        String rideId = matcher.group(1);
        setRideId(rideId);
        return true;
    }

    protected void setRideId(String rideId) {
        mRideId = rideId;
        if (mRideId == null || mRideId.isEmpty()) throw new Error("No Ride ID in RideDetailsActivity");
        Log.i("RideDetailsActivity", "Loaded Ride " + mRideId);
    }

    protected void initData(Bundle activityData) {
        if (activityData == null) return;
        setRideId(activityData.getString(RIDE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RIDE, mRideId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void initFragment() {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null)
            mFragment = (RideDetailsFragment) frags.get(0);
        if (mFragment == null) {
            mFragment = RideDetailsFragment.newInstance(mRideId);
            addFragmentToContainer(mFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ride, menu);

        MenuItem item = menu.findItem(R.id.share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Ride.createLink(mRideId));
        shareIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.event) {
            navigateToEventDetails(mFragment.getEventId());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbar(String cover, String title) {
        this.mTitle = title;
        setToolbarImage(cover);
        setToolbarTitle(mTitle);
    }
}
