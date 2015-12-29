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
import com.howest.nmct.bob.adapters.EventAdapter;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Nick on 28/10/2015.
 */
public class EventsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.empty_view) TextView emptyView;

    public EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int URL_LOADER = 0;

    private static final String[] EVENT_COLUMNS = {
            EventEntry.TABLE_NAME + "." + EventEntry._ID,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            EventEntry.COLUMN_START_TIME,
            EventEntry.COLUMN_COVER,
            PlaceEntry.TABLE_NAME + "." + PlaceEntry.COLUMN_NAME
    };

    public static final int COL_EVENT_ID = 0;
    public static final int COL_EVENT_NAME = 1;
    public static final int COL_EVENT_START_TIME = 2;
    public static final int COL_EVENT_COVER = 3;
    public static final int COL_PLACE_NAME = 4;

    public EventsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_events, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setLayoutManager(null);
    }

    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new EventAdapter(this);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }


    public void onEventSelected(String eventId, ImageView imgEvent) {
        ((NavigationActivity) getActivity())
                .navigateToEventDetails(eventId, imgEvent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        EventEntry.CONTENT_URI,
                        EVENT_COLUMNS,
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
        Log.d("EventsFragment", "Cursor loaded " + data.getCount());

        if (data.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(R.string.no_events);
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