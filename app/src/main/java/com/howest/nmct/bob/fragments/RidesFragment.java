package com.howest.nmct.bob.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import com.howest.nmct.bob.models.Ride;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
    private ArrayList<Ride> rides = new ArrayList<>();

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
        Ride tomorrowLand = new Ride("Tomorrowland", "20/10/2015", "Tomorrowstraat 123, Brussel");
        tomorrowLand.setImage("http://www.tomorrowland.com/sites/default/files/styles/image_gallery_full/public/media/Galleries/2013/BESTOF_Friday_-04.jpg");

        Ride gentseRide = new Ride("Gentse Feesten", "12/07/2016", "Korenmarkt, Gent");
        gentseRide.setImage("http://gentsefeesten.gent/sites/default/files/styles/eyecatcher/public/eyecatcher/image/Foto%2021%20mensenzee%20Korenmarkt.jpg?itok=5ci3hCrr");
        gentseRide.setApproved(10);
        tomorrowLand.setRequests(5);

        rides.add(tomorrowLand);
        rides.add(gentseRide);
        rides.add(tomorrowLand);
        rides.add(gentseRide);
        rides.add(tomorrowLand);
        rides.add(gentseRide);
        rides.add(tomorrowLand);
        rides.add(gentseRide);

        rides.add(new Ride("An event for great and spectacularious amazeballs photography with your fabulous unicorn!!! \uD83D\uDE04 \uD83D\uDC34 \uD83D\uDE04 \uD83D\uDC34 ", "12/12/2020", "Somewhere in '); DROP TABLE city;"));
    }

    /**
     * Sets up the RecyclerView;
     */
    private void initViews() {
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getActivity());

        if (mAdapter == null)
            mAdapter = new RideAdapter(this, rides);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Adapter for the RecyclerView
     */
    static class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
        private ViewGroup parent;
        private ArrayList<Ride> rides;
        private Context activity;

        public RideAdapter(RidesFragment ridesFragment, ArrayList<Ride> rides) {
            activity = ridesFragment.getContext();
            this.rides = rides;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ride_item, parent, false);
            return new ViewHolder(v);
        }

        /**
         * Sets badges active or inactive
         *
         * @param textView The badge to color
         * @param isActive If the active color should be set
         */
        public void setBadgeColor(TextView textView, Boolean isActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(isActive ? R.style.Badge_Active : R.style.Badge);
            } else {
                if (isActive) {
                    textView.setBackgroundResource(R.color.colorAccent);
                    textView.setTextColor(Color.WHITE);
                } else {
                    textView.setBackgroundColor(Color.TRANSPARENT);
                    textView.setTextColor(Color.BLACK);
                }
            }
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            Ride ride = rides.get(position);
            holder.rideTitle.setText(ride.getTitle());
            holder.rideDate.setText(ride.getDate());
            holder.rideAddress.setText(ride.getAddress());
            holder.valueApproved.setText(String.format("%s", ride.getApproved()));
            holder.valueRequests.setText(String.format("%s", ride.getRequests()));

            Picasso p = Picasso.with(activity);
            // Red = Network
            // Blue = Disk
            // Green = Memory
            p.setIndicatorsEnabled(true);
            p.load(ride.getImage())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_image)
                    .into(holder.rideImage);

            setBadgeColor(holder.valueApproved, ride.getApproved() > 0);
            setBadgeColor(holder.valueRequests, ride.getRequests() > 0);
        }

        @Override
        public int getItemCount() {
            return rides.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.ride_image)
            ImageView rideImage;
            @Bind(R.id.ride_title)
            TextView rideTitle;
            @Bind(R.id.ride_date)
            TextView rideDate;
            @Bind(R.id.ride_address)
            TextView rideAddress;
            @Bind(R.id.valueApproved)
            TextView valueApproved;
            @Bind(R.id.valueRequests)
            TextView valueRequests;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
