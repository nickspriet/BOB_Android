package com.howest.nmct.bob.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.RideDetailsActivity;
import com.howest.nmct.bob.adapters.UserAdapter;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.data.Contracts.UserRideEntry;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.UserRide;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
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
    @Bind(R.id.lstAccepted) ListView lstAccepted;
    @Bind(R.id.lstRequest) ListView lstRequest;
    @Bind(R.id.tvDriverCarModel) TextView tvDriverCarModel;
    @Bind(R.id.tvDriverCarNo) TextView tvDriverCarNo;

    private String mRideId;
    private static final int URL_LOADER = 0;
    private static final int APPROVED_LOADER = 1;
    private static final int REQUEST_LOADER = 2;
    private Cursor mCursor;

    private CursorAdapter mApprovedAdapter;
    private CursorAdapter mRequestAdapter;

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
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_PICTURE,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_CAR_MODEL,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_CAR_NO
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
    public static final int COL_DRIVER_CAR_MODEL = 11;
    public static final int COL_DRIVER_CAR_NO = 12;

    private static final String[] USER_RIDE_COLUMNS = {
            UserRideEntry.TABLE_NAME + "." + UserRideEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_NAME,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_PICTURE
    };

    public static final int COL_USER_RIDE_ID = 0;
    public static final int COL_USER_RIDE_USER_ID = 1;
    public static final int COL_USER_RIDE_USER_NAME = 2;
    public static final int COL_USER_RIDE_USER_PICTURE = 3;

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
        getLoaderManager().initLoader(APPROVED_LOADER, null, this);
        getLoaderManager().initLoader(REQUEST_LOADER, null, this);
    }

    public void setRide(String rideId) {
        this.mRideId = rideId;
        getLoaderManager().restartLoader(URL_LOADER, null, this);
        getLoaderManager().restartLoader(APPROVED_LOADER, null, this);
        getLoaderManager().restartLoader(REQUEST_LOADER, null, this);
    }

    public String getEventId() {
        return mCursor.getString(COL_EVENT_ID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApprovedAdapter = new UserAdapter(getContext(), COL_USER_RIDE_USER_NAME, COL_USER_RIDE_USER_PICTURE);
        mRequestAdapter = new UserAdapter(getContext(), COL_USER_RIDE_USER_NAME, COL_USER_RIDE_USER_PICTURE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_ride_details, container, false);
        ButterKnife.bind(this, view);
        initViews();
        lstAccepted.setEmptyView(inflater.inflate(R.layout.empty_user_list, container, false));
        lstRequest.setEmptyView(inflater.inflate(R.layout.empty_user_list, container, false));
        lstAccepted.setAdapter(mApprovedAdapter);
        lstRequest.setAdapter(mRequestAdapter);
        return view;
    }

    public void initViews() {
        if (mCursor == null) return;

        String locationName = mCursor.getString(COL_PLACE_NAME);
        Date startTime = Event.parseDate(mCursor.getString(COL_RIDE_START_TIME));

        tvAddress.setText(locationName);
        tvDate.setText(DateFormat.getDateTimeInstance().format(startTime));
        tvDescription.setText(mCursor.getString(COL_DESCRIPTION));
        tvDriverName.setText(mCursor.getString(COL_USER_NAME));
        tvDriverCarModel.setText(mCursor.getString(COL_DRIVER_CAR_MODEL));
        tvDriverCarNo.setText(mCursor.getString(COL_DRIVER_CAR_NO));

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
            case APPROVED_LOADER:
                return new CursorLoader(
                        getContext(),
                        UserRideEntry.CONTENT_URI,
                        USER_RIDE_COLUMNS,
                        UserRideEntry.COLUMN_RIDE_ID + "=? AND " + UserRideEntry.COLUMN_STATUS + "=?",
                        new String[] {mRideId, String.valueOf(UserRide.APPROVED)},
                        null
                );
            case REQUEST_LOADER:
                return new CursorLoader(
                        getContext(),
                        UserRideEntry.CONTENT_URI,
                        USER_RIDE_COLUMNS,
                        UserRideEntry.COLUMN_RIDE_ID + "=? AND " + UserRideEntry.COLUMN_STATUS + "=?",
                        new String[] {mRideId, String.valueOf(UserRide.REQUEST)},
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getLoaderManager().getLoader(URL_LOADER).equals(loader)) {
            Log.d("EventDetailsFragment", "Finished loading ride " + data.getCount());
            if (data.moveToFirst()) {
                mCursor = data;
                initViews();
                ((RideDetailsActivity) getActivity()).initToolbar(
                        mCursor.getString(COL_EVENT_COVER),
                        mCursor.getString(COL_EVENT_NAME));
            } else {
                Toast.makeText(getContext(), "No ride found", Toast.LENGTH_LONG).show();
            }
        } else if (getLoaderManager().getLoader(APPROVED_LOADER).equals(loader)) {
            Log.d("EventDetailsFragment", "Finished approved users " + data.getCount());
            if (data.moveToFirst()) {
                mApprovedAdapter.swapCursor(data);
            }
        } else if (getLoaderManager().getLoader(REQUEST_LOADER).equals(loader)) {
            Log.d("EventDetailsFragment", "Finished request users " + data.getCount());
            if (data.moveToFirst()) {
                mRequestAdapter.swapCursor(data);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mApprovedAdapter.swapCursor(null);
        mRequestAdapter.swapCursor(null);
    }
}
