package com.howest.nmct.bob.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nick on 28/10/2015.
 * Adapter for the events
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> mEvents;
    private MainActivity mActivity;
    private EventsFragment mFragment;

    public EventAdapter(EventsFragment eventsFragment, ArrayList<Event> events) {
        this.mActivity = (MainActivity) eventsFragment.getActivity();
        this.mFragment = eventsFragment;
        this.mEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v, this);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.tvEventDay.setText("" + event.getEventDateFormat("FF"));
        holder.tvEventMonth.setText(event.getEventDateFormat("MMM").toUpperCase());
        holder.tvEventName.setText(event.getEventName());
        holder.tvEventInfo.setText(event.getEventDateFormat("E h a") + " " + event.getEventAddress());
        holder.tvEventFriendsOrGuests.setText(event.getEventFriendsOrGuests());

        //set image via picasso
        Picasso p = Picasso.with(this.mActivity.getApplicationContext());
        p.setIndicatorsEnabled(true);
        p.load(event.getEventImage())
                .fit()
                .centerCrop()
                .into(holder.imgEvent);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    private void onEventGoing(int adapterPosition) {
        Event event = mEvents.get(adapterPosition);
        mFragment.onShowCreateRideDialog(event);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgEvent)  ImageView imgEvent;
        @Bind(R.id.tvEventDay)  TextView tvEventDay;
        @Bind(R.id.tvEventMonth)  TextView tvEventMonth;
        @Bind(R.id.tvEventName)  TextView tvEventName;
        @Bind(R.id.tvEventInfo)  TextView tvEventInfo;
        @Bind(R.id.tvEventFriendsOrGuests)  TextView tvEventFriendsOrGuests;

        private EventAdapter adapter;

        public ViewHolder(View view, EventAdapter adapter) {
            super(view);
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        /*
        @OnClick(R.id.btnEventGoing)
        public void onEventGoing() {
            adapter.onEventGoing(getAdapterPosition());
        }
        */

        @OnClick(R.id.cardView)
        public void onCardClicked() {
            adapter.onEventSelected(getAdapterPosition());
        }
    }

    private void onEventSelected(long itemId) {
        Event event = mEvents.get((int) itemId);
        mFragment.onEventSelected(event);
    }
}
