package com.howest.nmct.bob.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.EventDetailsActivity;
import com.howest.nmct.bob.models.Event;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {
    @Bind(R.id.tvEventDetailsAddress) TextView tvEventDetailsAddress;
    @Bind(R.id.tvEventDetailsStartTime) TextView tvEventDetailsStartTime;
    @Bind(R.id.tvEventDetailsEndTime) TextView tvEventDetailsEndTime;
    @Bind(R.id.tvEventDetailsFriendsAndGuests) TextView tvEventDetailsFriendsAndGuests;
    @Bind(R.id.tvDescription) TextView tvDescription;

    @Bind(R.id.btnGoing) Button btnGoing;
    @Bind(R.id.btnInterested) Button btnInterested;
    @Bind(R.id.btnNotGoing) Button btnNotGoing;

    @Bind(R.id.bobBadge) View bobBadge;
    @Bind(R.id.bobBadgeSeparator) View bobBadgeSeparator;

    @Bind(R.id.endsAtContainer) View endsAtContainer;
    @Bind(R.id.endsAtContainerSeparator) View endsAtContainerSeparator;

    @Bind(R.id.startAtContainer) View startAtContainer;
    @Bind(R.id.startAtContainerSeparator) View startAtContainerSeparator;

    @Bind(R.id.locationContainer) View locationContainer;
    @Bind(R.id.locationContainerSeparator) View locationContainerSeparator;

    private Event mEvent;
    public EventDetailsFragment() {}

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setEvent(event);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_event_details, container, false);

        ButterKnife.bind(this, v);
        initViews();

        return v;
    }

    private void setEvent(Event event) {
        this.mEvent = event;
    }

    private void initViews() {
        if (mEvent == null) return;

        tvEventDetailsFriendsAndGuests.setText(String.format("%s are going.",
                        mEvent.getAttendingCount()));

        bobBadge.setVisibility(View.GONE);
        bobBadgeSeparator.setVisibility(View.GONE);

        btnGoing.setText(String.format("Going\n(%d)", mEvent.getAttendingCount()));
        btnInterested.setText(String.format("Interested\n(%d)", mEvent.getInterestedCount()));
        btnNotGoing.setText(String.format("Not Going\n(%d)", mEvent.getDeclinedCount()));

        if (!mEvent.getAddress().isEmpty()) {
            tvEventDetailsAddress.setText(mEvent.getAddress());
        } else {
            locationContainer.setVisibility(View.GONE);
            locationContainerSeparator.setVisibility(View.GONE);
        }


        Date startTime = mEvent.getStartTime();
        if (startTime != null) {
            tvEventDetailsStartTime.setText(String.format("%s %s at %s",
                    mEvent.getEventDateFormat("EEE", startTime),
                    mEvent.getEventDateFormat("FF", startTime),
                    mEvent.getEventDateFormat("hh:mm a", startTime)));
        } else {
            startAtContainer.setVisibility(View.GONE);
            startAtContainerSeparator.setVisibility(View.GONE);
        }

        Date endTime = mEvent.getEndTime();
        if (endTime != null) {
            tvEventDetailsEndTime.setText(String.format("%s %s at %s",
                    mEvent.getEventDateFormat("EEE", endTime),
                    mEvent.getEventDateFormat("FF", endTime),
                    mEvent.getEventDateFormat("hh:mm a", endTime)));
        } else {
            endsAtContainer.setVisibility(View.GONE);
            endsAtContainerSeparator.setVisibility(View.GONE);
        }

        tvDescription.setText(mEvent.getDescription());
    }

    @OnClick(R.id.btnGoing)
    public void onBtnGoingClick() {
        EventDetailsActivity activity = (EventDetailsActivity) getActivity();
        activity.onGoing();
    }

    @OnClick(R.id.btnInterested)
    public void onBtnInterestedClick() {
        EventDetailsActivity activity = (EventDetailsActivity) getActivity();
        activity.onInterested();
    }

    @OnClick(R.id.btnGoing)
    public void onBtnNotGoingClick() {
        EventDetailsActivity activity = (EventDetailsActivity) getActivity();
        activity.onNotGoing();
    }
}









