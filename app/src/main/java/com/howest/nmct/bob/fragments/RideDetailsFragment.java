package com.howest.nmct.bob.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.RideDetailsActivity;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * illyism
 * 22/10/15
 */
public class RideDetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.address) TextView tvAddress;
    @Bind(R.id.date) TextView tvDate;
    @Bind(R.id.description) TextView tvDescription;
    @Bind(R.id.imgDriverProfile) CircleImageView imgDriverProfile;
    @Bind(R.id.tvDriverName) TextView tvDriverName;

    private String mRideId;
    private static final int URL_LOADER = 0;
    private Cursor mCursor;

    private static final String[] RIDE_COLUMNS = {
            RideEntry.TABLE_NAME + "." + RideEntry._ID,
            EventEntry.TABLE_NAME + "." + EventEntry._ID,
            PlaceEntry.TABLE_NAME + "." + PlaceEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_START_TIME,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_COVER,
            PlaceEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_DESCRIPTION,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_PICTURE
    };

    public static final int COL_RIDE_ID = 0;
    public static final int COL_EVENT_ID = 1;
    public static final int COL_PLACE_ID = 2;
    public static final int COL_USER_ID = 3;
    public static final int COL_RIDE_START_TIME = 4;
    public static final int COL_USER_NAME = 5;
    public static final int COL_EVENT_NAME = 6;
    public static final int COL_EVENT_COVER = 7;
    public static final int COL_PLACE_NAME = 8;
    public static final int COL_DESCRIPTION = 9;
    public static final int COL_DRIVER_PICTURE = 10;

    public RideDetailsFragment() {}

    public static RideDetailsFragment newInstance(String rideId) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        fragment.mRideId = rideId;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    public void setRide(String rideId) {
        this.mRideId = rideId;
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    public String getEventId() {
        return mCursor.getString(COL_EVENT_ID);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_ride_details, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void initViews() {
        if (mCursor == null) return;

        String locationName = mCursor.getString(RidesFragment.COL_PLACE_NAME);
        Date startTime = Event.parseDate(mCursor.getString(RidesFragment.COL_RIDE_START_TIME));

        tvAddress.setText(locationName);
        tvDate.setText(Event.formatDate("E h a", startTime));
        tvDescription.setText(mCursor.getString(COL_DESCRIPTION));
        tvDriverName.setText(mCursor.getString(COL_USER_NAME));

        Picasso p = Picasso.with(getActivity());
        p.load(mCursor.getString(COL_DRIVER_PICTURE))
                .fit()
                .centerCrop()
                .into(imgDriverProfile);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getContext(),
                        RideEntry.buildRideUri(mRideId),
                        RIDE_COLUMNS,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("EventDetailsActivity", "Finished loading ride " + data.getCount());
        if (data.moveToFirst()) {
            mCursor = data;
            initViews();
            ((RideDetailsActivity) getActivity()).initToolbar(
                    mCursor.getString(COL_EVENT_COVER),
                    mCursor.getString(COL_EVENT_NAME));
        } else {
            Toast.makeText(getContext(), "No ride found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
