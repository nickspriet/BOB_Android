package com.howest.nmct.bob.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.Constants;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.models.User;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

public class MainActivity extends NavigationActivity {
    public User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(Constants.USER_PROFILE);
        }
        if (mUser == null) {
            Bundle b = getIntent().getExtras();
            mUser = b.getParcelable(Constants.USER_PROFILE);
        }

        ButterKnife.bind(this);
        super.initNavigation();
        initData();
        initDrawerHeader();

        if (savedInstanceState == null) navigateToEvents();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.USER_PROFILE, mUser);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUser = savedInstanceState.getParcelable(Constants.USER_PROFILE);
    }

    /**
     * Populates the Ride ArrayList
     */
    private void initData() {
        Rides.fetchData();
    }


    /**
     * Sets the navigation drawer header for the logged in profile
     */
    public void initDrawerHeader() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Events when selecting an item in the options
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets the image and title of the collapsing toolbar
     * @param url URL of the image to set
     * @param title Title of the toolbar to set
     */
    public void setToolbarImage(String url, String title) {
        Picasso p = Picasso.with(this);
        p.setIndicatorsEnabled(true);
        p.load(url)
                .fit()
                .centerCrop()
                .into(mToolbarImage);
        mToolbarImage.setVisibility(View.VISIBLE);
        mToolbarOverlay.setVisibility(View.VISIBLE);
        mToolbarLayout.setTitle(title);
    }

    /**
     * Removes the image of the collapsing toolbar
     */
    public void clearToolbar() {
        mToolbarOverlay.setVisibility(View.GONE);
        mToolbarImage.setImageResource(0);
        mToolbarImage.setVisibility(View.GONE);
    }
}
