package com.howest.nmct.bob.adapters;

/**
 * illyism
 * 22/10/15
 */

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Ride;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter for the RecyclerView
 */
public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    private ArrayList<Ride> rides;
    private MainActivity activity;
    private RidesFragment fragment;

    public RideAdapter(RidesFragment ridesFragment, ArrayList<Ride> rides) {
        this.activity = (MainActivity) ridesFragment.getActivity();
        this.fragment = ridesFragment;
        this.rides = rides;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_item, parent, false);
        return new ViewHolder(v, this);
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

    private void onClickImage(long itemId) {
        Ride ride = rides.get((int) itemId);
        fragment.onRideSelected(ride);
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

        private RideAdapter adapter;

        public ViewHolder(View view, RideAdapter adapter) {
            super(view);
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.ride_image)
        public void onClickImage() {
            adapter.onClickImage(getAdapterPosition());
        }
    }
}
