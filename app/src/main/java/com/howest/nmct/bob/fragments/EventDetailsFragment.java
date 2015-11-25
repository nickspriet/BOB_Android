package com.howest.nmct.bob.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.Event;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {
    @Bind(R.id.tvEventDetailsAddress) TextView tvEventDetailsAddress;
    @Bind(R.id.tvEventDetailsTotalTime) TextView tvEventDetailsTotalTime;

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

        tvEventDetailsAddress.setText(mEvent.getEventAddress());
        tvEventDetailsTotalTime.setText(mEvent.getEventDateFormat("EEE") + " " + mEvent.getEventDateFormat("FF") + " at " + mEvent.getEventDateFormat("hh:mm a"));
    }
}









