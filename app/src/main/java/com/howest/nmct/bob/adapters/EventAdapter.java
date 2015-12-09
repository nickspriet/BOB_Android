package com.howest.nmct.bob.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.BaseActivity;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nick on 28/10/2015.
 * Adapter for the events
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private ArrayList<Event> mEvents;
    private final BaseActivity mActivity;
    private final EventsFragment mFragment;

    public EventAdapter(EventsFragment eventsFragment, LinkedHashSet<Event> events) {
        this.mActivity = (BaseActivity) eventsFragment.getActivity();
        this.mFragment = eventsFragment;
        this.mEvents = new ArrayList<>(events);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v, this);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        Date startTime = event.getStartTime();
        holder.tvEventDay.setText(event.getEventDateFormat("FF", startTime));
        holder.tvEventMonth.setText(event.getEventDateFormat("MMM", startTime).toUpperCase());
        holder.tvEventName.setText(event.getName());
        holder.tvEventDate.setText(event.getEventDateFormat("E h a", startTime) );
        holder.tvEventLocation.setText(event.getAddress());

        //set image via picasso
        Picasso p = Picasso.with(this.mActivity.getApplicationContext());
        p.setIndicatorsEnabled(true);
        p.load(event.getCover())
                .fit()
                .centerCrop()
                .into(holder.imgEvent);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void setEvents(LinkedHashSet<Event> events) {
        this.mEvents = new ArrayList<>(events);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgEvent)  ImageView imgEvent;
        @Bind(R.id.tvEventDay)  TextView tvEventDay;
        @Bind(R.id.tvEventMonth)  TextView tvEventMonth;
        @Bind(R.id.tvEventName)  TextView tvEventName;
        @Bind(R.id.tvEventDate)  TextView tvEventDate;
        @Bind(R.id.tvEventLocation)  TextView tvEventLocation;

        private final EventAdapter adapter;

        public ViewHolder(View view, EventAdapter adapter) {
            super(view);
            this.adapter = adapter;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardView)
        public void onCardClicked() {
            ViewCompat.setTransitionName(imgEvent, "toolbarImage");
            adapter.onEventSelected(getAdapterPosition(), imgEvent);
        }
    }

    private void onEventSelected(long itemId, ImageView imgEvent) {
        Event event = mEvents.get((int) itemId);
        mFragment.onEventSelected(event, imgEvent);
    }
}
