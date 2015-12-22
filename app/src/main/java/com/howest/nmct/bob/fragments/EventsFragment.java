package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.NavigationActivity;
import com.howest.nmct.bob.adapters.EventAdapter;
import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.interfaces.EventsLoadedListener;
import com.howest.nmct.bob.models.Event;

import java.io.IOException;
import java.util.LinkedHashSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Nick on 28/10/2015.
 */
public class EventsFragment extends Fragment implements EventsLoadedListener {
    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.empty_view) TextView emptyView;
    public EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public EventsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_events, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
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
            mAdapter = new EventAdapter(this, Events.getEvents());

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(mAdapter);
        }
    }


    public void onEventSelected(Event event, ImageView imgEvent) {
        ((NavigationActivity) getActivity())
                .navigateToEventDetails(event, imgEvent);
    }

    public void startLoading() {
        emptyView.setText(R.string.loading_events);
    }

    @Override
    public void failedLoading(IOException e) {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setText(R.string.failed_loading);
    }

    @Override
    public void eventsLoaded(LinkedHashSet<Event> events) {
        if (events == null || events.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyView.setText(R.string.no_events);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter.setEvents(events);
            mAdapter.notifyDataSetChanged();
        }
    }
}