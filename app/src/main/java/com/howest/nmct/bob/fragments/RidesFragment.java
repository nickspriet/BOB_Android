package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.adapters.RideAdapter;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.models.Ride;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 21/10/15
 */
public class RidesFragment extends Fragment {
    @Bind(R.id.list)
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RidesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_rides, container, false);
        ButterKnife.bind(this, view);
        initData();
        initViews();
        return view;
    }

    /**
     * Populates the Ride ArrayList
     */
    private void initData() {
        Ride tomorrowLand = new Ride("1", "Tomorrowland", "20/10/2015", "Tomorrowstraat 123, Brussel");
        tomorrowLand.setImage("http://www.tomorrowland.com/sites/default/files/styles/image_gallery_full/public/media/Galleries/2013/BESTOF_Friday_-04.jpg");

        Ride gentseRide = new Ride("2", "Gentse Feesten", "12/07/2016", "Korenmarkt, Gent");
        gentseRide.setImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");
        gentseRide.setApproved(10);
        tomorrowLand.setRequests(5);

        Ride randomRide = new Ride("3", "An event for great and spectacularious amazeballs photography with your fabulous unicorn!!! \uD83D\uDE04 \uD83D\uDC34 \uD83D\uDE04 \uD83D\uDC34 ", "12/12/2020", "Somewhere in '); DROP TABLE city;");

        Rides.addRides(
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                gentseRide,
                tomorrowLand,
                randomRide
        );
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
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void onRideSelected(Ride ride) {
        ((MainActivity) getActivity()).navigatetoRideDetails(ride);
    }
}
