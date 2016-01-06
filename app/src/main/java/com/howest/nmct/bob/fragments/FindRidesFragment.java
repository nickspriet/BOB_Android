package com.howest.nmct.bob.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.FindRidesActivity;
import com.howest.nmct.bob.adapters.UserAdapter;
import com.howest.nmct.bob.api.APIRidesResponse;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.models.Ride;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;

/**
 * illyism
 * 21/10/15
 */
public class FindRidesFragment extends Fragment {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    @Bind(R.id.lstRides) ListView lstRides;

    public UserAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
    }

    private static final String[] RIDE_COLUMNS = {
            BaseColumns._ID,
            RideEntry.TABLE_NAME + "." + RideEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_NAME,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_PICTURE
    };

    public static final int COL_RIDE_ID = 1;
    public static final int COL_USER_ID = 2;
    public static final int COL_USER_NAME = 3;
    public static final int COL_USER_PICTURE = 4;

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
        loadData();
        this.mActivity = (FindRidesActivity) getActivity();
    }

    private void loadData() {
        OkHttpClient okHttpClient = new OkHttpClient();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = preferences.getString(BACKEND_TOKEN, "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(BACKEND_SCHEME)
                .encodedAuthority(BACKEND_HOST)
                .appendPath("api")
                .appendPath("event")
                .appendPath(mEventId)
                .appendPath("ride")
                .appendQueryParameter("token", token);

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e("FindRidesFragment", "Find Rides failed", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                APIRidesResponse apiResponse = new Gson().fromJson(response.body().charStream(), APIRidesResponse.class);

                final MatrixCursor c = new MatrixCursor(RIDE_COLUMNS);

                int i = 0;
                for (Ride r : apiResponse.data.rides) {
                    c.newRow()
                        .add(i++)
                        .add(r.id)
                        .add(r.driver.Id)
                        .add(r.driver.name)
                        .add(r.driver.picture);
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onLoadFinished(c);
                    }
                });
            }
        });
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

    public void onLoadFinished(Cursor data) {
        Log.d("FindRidesFragment", "Rides loaded " + data.getCount());
        this.mCursor = data;
        mAdapter.swapCursor(mCursor);
    }
}
