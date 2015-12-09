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
import com.howest.nmct.bob.activities.RidesActivity;
import com.howest.nmct.bob.adapters.RideAdapter;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.utils.IntentStarter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class RidesFragment extends Fragment {
    @Bind(R.id.list) RecyclerView recyclerView;
    public RideAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RidesFragment() {
    }

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

    /**
     * Sets up the RecyclerView;
     */
    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new RideAdapter(this, Rides.getRides());

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void onRideSelected(Ride ride, ImageView rideImage) {
        ((NavigationActivity) getActivity())
                .navigateToRideDetails(ride, rideImage);
    }

    public void onRideMapClick(Ride ride) {
        IntentStarter.openGoogleMaps(getContext(), ride.getAddress());
    }

    public void onRideEventClick(Ride ride) {
        ((RidesActivity) getActivity()).navigateToEvents();
    }
}
