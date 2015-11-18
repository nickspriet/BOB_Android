package com.howest.nmct.bob.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howest.nmct.bob.MainActivity;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.models.Event;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nick on 28/10/2015.
 * Adapter for the events
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> events;
    private MainActivity activity;
    private EventsFragment fragment;

    public EventAdapter(EventsFragment eventsFragment, ArrayList<Event> events) {
        this.activity = (MainActivity) eventsFragment.getActivity();
        this.fragment = eventsFragment;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v, this);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.tvEventName.setText(event.getEventName());
        holder.tvEventDate.setText(event.getEventDate());
        holder.tvEventAddress.setText(event.getEventAddress());


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void onEventGoing(int adapterPosition) {
        Event event = events.get(adapterPosition);
        fragment.onShowCreateRideDialog(event);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvEventName) TextView tvEventName;
        @Bind(R.id.tvEventDate) TextView tvEventDate;
        @Bind(R.id.tvEventAddress) TextView tvEventAddress;

        private EventAdapter adapter;

        public ViewHolder(View view, EventAdapter adapter) {
            super(view);
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.btnEventGoing)
        public void onEventGoing() {
            adapter.onEventGoing(getAdapterPosition());
        }
    }


}
