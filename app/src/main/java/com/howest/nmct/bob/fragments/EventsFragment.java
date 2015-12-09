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

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.NavigationActivity;
import com.howest.nmct.bob.adapters.EventAdapter;
import com.howest.nmct.bob.collections.Events;
import com.howest.nmct.bob.models.Event;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Nick on 28/10/2015.
 */
public class EventsFragment extends Fragment {
    @Bind(R.id.list) RecyclerView recyclerView;
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
}