package com.howest.nmct.bob.fragments;

import android.content.Intent;
import android.net.Uri;
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

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class RidesFragment extends Fragment {
    @Bind(R.id.list) RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
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
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s", ride.getAddress());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);
    }

    public void onRideEventClick(Ride ride) {
        ((RidesActivity) getActivity()).navigateToEvents();
    }
}
