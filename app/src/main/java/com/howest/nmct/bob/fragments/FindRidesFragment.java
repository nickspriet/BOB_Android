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
import android.widget.AdapterView;
import android.widget.ListView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.FindRidesActivity;
import com.howest.nmct.bob.adapters.UserAdapter;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class FindRidesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.lstRides) ListView lstRides;

    public UserAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int URL_LOADER = 0;
    private String mEventId;
    private FindRidesActivity mActivity;
    private Cursor mCursor;

    public static FindRidesFragment newInstance(String eventId) {
        if (eventId == null || eventId.isEmpty()) {
            Log.e("FindRidesFragment", "No event id received.");
        }
        FindRidesFragment fragment = new FindRidesFragment();
        fragment.mEventId = eventId;
        return fragment;
    }

    public void setEvent(String eventId) {
        this.mEventId = eventId;
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

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
            "(SELECT count(*) FROM userride WHERE ride._id=userride.ride_id AND userride.status=1)",
            "(SELECT count(*) FROM userride WHERE ride._id=userride.ride_id AND userride.status=2)",
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
    public static final int COL_USER_RIDE_APPROVED_COUNT = 9;
    public static final int COL_USER_RIDE_REQUEST_COUNT = 10;
    public static final int COL_USER_PICTURE = 11;

    public FindRidesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_find_rides, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        this.mActivity = (FindRidesActivity) getActivity();
    }

    /**
     * Sets up the RecyclerView;
     */
    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new UserAdapter(getContext(), COL_USER_NAME, COL_USER_PICTURE);

        if (lstRides != null) {
            lstRides.setEmptyView(View.inflate(getContext(), R.layout.empty_view, null));
            lstRides.setAdapter(mAdapter);
            lstRides.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mActivity != null) {
                        mCursor.moveToPosition(position);
                        mActivity.onRideSelected(mCursor.getString(COL_RIDE_ID));
                    }
                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getContext(),
                        RideEntry.CONTENT_URI,
                        RIDE_COLUMNS,
                        RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_EVENT_ID + "=?",
                        new String[] {mEventId},
                        RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_START_TIME + " DESC"
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("FindRidesFragment", "Rides loaded " + data.getCount());
        this.mCursor = data;
        mAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mAdapter.swapCursor(null);
    }
}
