package com.howest.nmct.bob.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.MainActivity;
import com.howest.nmct.bob.models.Ride;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * illyism
 * 22/10/15
 */
public class RideDetailsFragment extends Fragment {
    @Bind(R.id.address) TextView tvAddress;
    @Bind(R.id.date) TextView tvDate;

    private Ride mRide;

    public RideDetailsFragment() {
    }

    public static RideDetailsFragment newInstance(Ride ride) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        fragment.setRide(ride);
        return fragment;
    }

    public void setRide(Ride ride) {
        this.mRide = ride;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_ride_details, container, false);

        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).clearToolbar();
    }


    private void initViews() {
        if (mRide == null) return;

        tvAddress.setText(mRide.getAddress());
        tvDate.setText(mRide.getDate());

        ((MainActivity) getActivity()).setToolbarImage(mRide.getImage(), mRide.getTitle());
    }
}
