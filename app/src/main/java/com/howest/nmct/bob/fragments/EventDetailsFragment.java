package com.howest.nmct.bob.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {
    @Bind(R.id.imgEventDetails) ImageView imgEventDetails;
    @Bind(R.id.tvEventDetailsName) TextView tvEventDetailsName;
    @Bind(R.id.tvEventDetailsAddress) TextView tvEventDetailsAddress;
    @Bind(R.id.tvEventDetailsTotalTime) TextView tvEventDetailsTotalTime;

    private Event mEvent;


    public EventDetailsFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(Event event) {
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


    public void setEvent(Event event) {
        this.mEvent = event;
    }

    private void initViews() {
        if (mEvent == null) return;

        tvEventDetailsName.setText(mEvent.getEventName());
        tvEventDetailsAddress.setText(mEvent.getEventAddress());
        tvEventDetailsTotalTime.setText(mEvent.getEventDateFormat("EEE") + " " + mEvent.getEventDateFormat("FF") + " at " + mEvent.getEventDateFormat("hh:mm a"));


        //set image via picasso
        Picasso p = Picasso.with(getContext());
        p.setIndicatorsEnabled(true);
        p.load(mEvent.getEventImage())
                .fit()
                .centerCrop()
                .into(imgEventDetails);

    }
}









