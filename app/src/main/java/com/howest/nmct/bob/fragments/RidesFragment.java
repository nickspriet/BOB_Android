package com.howest.nmct.bob.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.NavigationActivity;
import com.howest.nmct.bob.activities.RidesActivity;
import com.howest.nmct.bob.adapters.RideAdapter;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.interfaces.APIFetchListener;
import com.howest.nmct.bob.utils.IntentStarter;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class RidesFragment extends Fragment implements APIFetchListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.empty_view) TextView emptyView;

    public RideAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int URL_LOADER = 0;

    private static final String[] RIDE_COLUMNS = {
            RideEntry.TABLE_NAME + "." + RideEntry._ID,
            EventEntry.TABLE_NAME + "." + EventEntry._ID,
            PlaceEntry.TABLE_NAME + "." + PlaceEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_START_TIME,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_COVER,
            PlaceEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME
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

    public RidesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_rides, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setLayoutManager(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    /**
     * Sets up the RecyclerView;
     */
    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new RideAdapter(this);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void onRideSelected(String rideId, ImageView rideImage) {
        ((NavigationActivity) getActivity())
                .navigateToRideDetails(rideId, rideImage);
    }

    public void onRideMapClick(String placeId) {
        IntentStarter.openGoogleMaps(getContext(), placeId);
    }

    public void onRideEventClick(String eventId) {
        ((RidesActivity) getActivity()).navigateToEventDetails(eventId);
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void failedLoading(IOException e) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        RideEntry.CONTENT_URI,
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
        Log.d("RidesFragment", "Cursor loaded " + data.getCount());

        if (data.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(R.string.no_rides);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
