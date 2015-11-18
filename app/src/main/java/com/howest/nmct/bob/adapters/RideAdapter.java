package com.howest.nmct.bob.adapters;

/**
 * illyism
 * 22/10/15
 */

import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.views.AutoHeightViewPager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter for the RecyclerView
 */
public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    private ArrayList<Ride> mRides;
    private MainActivity mActivity;
    private RidesFragment mFragment;

    private enum SwipedState {
        SHOWING_PRIMARY_CONTENT,
        SHOWING_SECONDARY_CONTENT
    }
    private List<SwipedState> mItemSwipedStates;

    public RideAdapter(RidesFragment ridesFragment, ArrayList<Ride> rides) {
        mActivity = (MainActivity) ridesFragment.getActivity();
        mFragment = ridesFragment;
        mRides = rides;

        mItemSwipedStates = new ArrayList<>();
        for (int i = 0; i < mRides.size(); i++) {
            mItemSwipedStates.add(i, SwipedState.SHOWING_PRIMARY_CONTENT);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AutoHeightViewPager v = (AutoHeightViewPager) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_item, parent, false);

        ViewPagerAdapter adapter = new ViewPagerAdapter();
        ((ViewPager) v.findViewById(R.id.viewPager)).setAdapter(adapter);
        ViewHolder vh = new ViewHolder(v, this);

        // The ViewPager loses its width information when it is put
        // inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
        // screen.
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        v.getLayoutParams().width = displayMetrics.widthPixels;
        v.requestLayout();

        return vh;
    }

    /**
     * Sets the username as highlighted depending on if the user is the driver
     *
     * @param textView The textview
     * @param isDriver If the driver is the current logged in user
     */
    private void setUsernameColor(TextView textView, Boolean isDriver) {
        textView.setTextColor(isDriver ? Color.parseColor("#2196f3") : Color.BLACK);
    }

    /**
     * Hides or shows the view
     *
     * @param view The view to hide or show
     * @param isVisible If the driver is the current logged in user
     */
    private void setVisibility(View view, Boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }



    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Get information
        User profile = mActivity.mUser;
        Ride ride = mRides.get(position);

        // Fill in details
        holder.rideTitle.setText(ride.getTitle());
        holder.locationDetails.setText(Html.fromHtml(String.format("<b>%s</b> in <b>%s</b>", ride.getAddress(), ride.getDate())));
        holder.approvalStatus.setText(Ride.formatApprovalStatus(ride, profile));
        holder.ridePerson.setText(ride.getDriver().getName());

        Picasso p = Picasso.with(mActivity);
        // Red = Network
        // Blue = Disk
        // Green = Memory
        p.setIndicatorsEnabled(true);
        p.load(ride.getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .into(holder.rideImage);

        // Hide or show elements depending on approval and driver status
        setUsernameColor(holder.ridePerson, ride.isSelfDriver(profile));
        setVisibility(holder.locationDetails, ride.isSelfDriver(profile) || ride.isApproved(profile));
        setVisibility(holder.driverButton, !ride.isSelfDriver(profile));
        setVisibility(holder.mapButton, ride.isSelfDriver(profile) || ride.isApproved(profile));
        setVisibility(holder.guestsButton, ride.isSelfDriver(profile) || ride.isApproved(profile));

        // Set up viewpager
        holder.view.setCurrentItem(mItemSwipedStates.get(position).ordinal());
        holder.view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int previousPagePosition = 0;

            @Override
            public void onPageScrolled(int pagePosition, float positionOffset, int positionOffsetPixels) {
                if (pagePosition == previousPagePosition)
                    return;

                switch (pagePosition) {
                    case 0:
                        mItemSwipedStates.set(position, SwipedState.SHOWING_PRIMARY_CONTENT);
                        break;
                    case 1:
                        mItemSwipedStates.set(position, SwipedState.SHOWING_SECONDARY_CONTENT);
                        break;

                }
                previousPagePosition = pagePosition;
            }

            @Override
            public void onPageSelected(int pagePosition) {
                //This method keep incorrectly firing as the RecyclerView scrolls.
                //Use the one above instead
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }




    @Override
    public int getItemCount() {
        return mRides.size();
    }

    private void onRideSelected(long itemId, ImageView rideImage) {
        Ride ride = mRides.get((int) itemId);
        mFragment.onRideSelected(ride, rideImage);
    }

    private void onMapButtonClicked(long itemId) {
        Ride ride = mRides.get((int) itemId);
        mFragment.onRideMapClick(ride);
    }

    private void onEventButtonClicked(int adapterPosition) {
        Ride ride = mRides.get(adapterPosition);
        mFragment.onRideEventClick(ride);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ride_person) TextView ridePerson;
        @Bind(R.id.ride_image) ImageView rideImage;
        @Bind(R.id.ride_title) TextView rideTitle;
        @Bind(R.id.location_details) TextView locationDetails;
        @Bind(R.id.approval_status) TextView approvalStatus;

        @Bind(R.id.driver_button) LinearLayout driverButton;
        @Bind(R.id.map_button) LinearLayout mapButton;
        @Bind(R.id.guests_button) LinearLayout guestsButton;

        private ViewPager view;
        private RideAdapter adapter;

        public ViewHolder(ViewPager view, RideAdapter adapter) {
            super(view);
            this.view = view;
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardView)
        public void onCardClicked() {
            adapter.onRideSelected(getAdapterPosition(), rideImage);
        }

        @OnClick(R.id.map_button)
        public void onMapButtonClicked() {
            adapter.onMapButtonClicked(getAdapterPosition());
        }

        @OnClick(R.id.event_button)
        public void onEventButtonClicked() {
            adapter.onEventButtonClicked(getAdapterPosition());
        }
    }




}
