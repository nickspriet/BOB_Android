package com.howest.nmct.bob.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
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

    public void onRideSelected(Ride ride) {
        View v = getView();
        if (v == null) return;

        FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout);
        // frameLayout is er als er landscape mode is
        if (frameLayout != null) {
            ((MainActivity) getActivity()).navigatetoRideDetails(R.id.frameLayout, ride);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(size.x / 3, RecyclerView.LayoutParams.MATCH_PARENT));
        } else {
            ((MainActivity) getActivity()).navigatetoRideDetails(ride);
        }
    }

    public void onRideMapClick(Ride ride) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s", ride.getAddress());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);
    }

    public void onRideEventClick(Ride ride) {
        ((MainActivity) getActivity()).navigateToEvents();
    }
}
