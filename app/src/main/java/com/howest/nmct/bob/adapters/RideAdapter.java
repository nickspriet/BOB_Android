package com.howest.nmct.bob.adapters;

/**
 * illyism
 * 22/10/15
 */

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
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

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.BaseActivity;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Event;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;
import com.howest.nmct.bob.views.AutoHeightViewPager;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter for the RecyclerView
 */
public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    private Cursor mCursor;
    private final BaseActivity mActivity;
    private final RidesFragment mFragment;

    public void swapCursor(Cursor data) {
        if (mCursor == data) return;
        Cursor oldCursor = mCursor;
        mCursor = data;
        if (data != null) {
            resetSwipeStates();
            notifyDataSetChanged();
        }
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private enum SwipedState {
        SHOWING_PRIMARY_CONTENT,
        SHOWING_SECONDARY_CONTENT
    }
    private List<SwipedState> mItemSwipedStates;

    public RideAdapter(RidesFragment ridesFragment) {
        this.mActivity = (BaseActivity) ridesFragment.getActivity();
        this.mFragment = ridesFragment;
        resetSwipeStates();
    }

    public void resetSwipeStates() {
        mItemSwipedStates = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
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
        mCursor.moveToPosition(position);
        User profile = mActivity.getUser();
        Date startTime = Event.parseDate(mCursor.getString(RidesFragment.COL_RIDE_START_TIME));

        // Fill in details
        holder.rideTitle.setText(mCursor.getString(RidesFragment.COL_EVENT_NAME));

        String locationName = mCursor.getString(RidesFragment.COL_PLACE_NAME);
        holder.locationDetails.setText(
                Html.fromHtml(String.format("<b>%s</b> at <b>%s</b>",
                        DateFormat.getDateTimeInstance().format(startTime),
                        locationName
                ))
        );
        holder.shortDate.setText(Event.formatDate("d MMM\nyyyy", startTime));
        holder.ridePerson.setText(mCursor.getString(RidesFragment.COL_USER_NAME));

        Picasso p = Picasso.with(mActivity);
        p.load(mCursor.getString(RidesFragment.COL_EVENT_COVER))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .into(holder.rideImage);

        if (profile != null) {
            String driverId = mCursor.getString(RidesFragment.COL_USER_ID);
            Boolean isDriver = driverId.equals(profile.Id);
            Boolean isApproved = false;

            int approvedCount = mCursor.getInt(RidesFragment.COL_USER_RIDE_APPROVED_COUNT);
            int requestCount = mCursor.getInt(RidesFragment.COL_USER_RIDE_REQUEST_COUNT);
            // Hide or show elements depending on approval and driver status
            holder.approvalStatus.setText(Ride.formatApprovalStatus(isDriver, isApproved, approvedCount, requestCount));

            setUsernameColor(holder.ridePerson, isDriver );
            setVisibility(holder.locationDetails, isDriver || isApproved);
            setVisibility(holder.driverButton, !isDriver);
            setVisibility(holder.mapButton, isDriver || isApproved);
            setVisibility(holder.guestsButton, isDriver || isApproved);
        }

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
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    private void onRideSelected(int itemId) {
        mCursor.moveToPosition(itemId);
        mFragment.onRideSelected(mCursor.getString(RidesFragment.COL_RIDE_ID));
    }

    private void onMapButtonClicked(int itemId) {
        mCursor.moveToPosition(itemId);
        mFragment.onRideMapClick(mCursor.getString(RidesFragment.COL_PLACE_ID));
    }

    private void onEventButtonClicked(int adapterPosition) {
        mCursor.moveToPosition(adapterPosition);
        mFragment.onRideEventClick(mCursor.getString(RidesFragment.COL_EVENT_ID));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ride_person) TextView ridePerson;
        @Bind(R.id.ride_image) ImageView rideImage;
        @Bind(R.id.ride_title) TextView rideTitle;
        @Bind(R.id.location_details) TextView locationDetails;
        @Bind(R.id.approval_status) TextView approvalStatus;
        @Bind(R.id.shortDate) TextView shortDate;

        @Bind(R.id.driver_button) LinearLayout driverButton;
        @Bind(R.id.map_button) LinearLayout mapButton;
        @Bind(R.id.guests_button) LinearLayout guestsButton;

        private final ViewPager view;
        private final RideAdapter adapter;

        public ViewHolder(ViewPager view, RideAdapter adapter) {
            super(view);
            this.view = view;
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardView)
        public void onCardClicked() {
            adapter.onRideSelected(getAdapterPosition());
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
