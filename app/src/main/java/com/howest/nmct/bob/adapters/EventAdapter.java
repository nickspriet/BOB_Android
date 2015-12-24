package com.howest.nmct.bob.adapters;

import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.BaseActivity;
import com.howest.nmct.bob.data.EventsContract;
import com.howest.nmct.bob.fragments.EventsFragment;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nick on 28/10/2015.
 * Adapter for the events
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Cursor mCursor;
    private final BaseActivity mActivity;
    private final EventsFragment mFragment;

    public EventAdapter(EventsFragment eventsFragment) {
        this.mActivity = (BaseActivity) eventsFragment.getActivity();
        this.mFragment = eventsFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(v, this);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Date startTime = Event.parseDate(mCursor.getString(EventsFragment.COL_EVENT_START_TIME));
        
        holder.tvEventDay.setText(Event.formatDate("FF", startTime));
        holder.tvEventMonth.setText(Event.formatDate("MMM", startTime).toUpperCase());
        holder.tvEventName.setText(mCursor.getString(EventsFragment.COL_EVENT_NAME));
        holder.tvEventDate.setText(Event.formatDate("E h a", startTime) );
        // holder.tvEventLocation.setText(event.getAddress());

        //set image via picasso
        Picasso p = Picasso.with(this.mActivity.getApplicationContext());
        p.load(mCursor.getString(EventsFragment.COL_EVENT_COVER))
                .fit()
                .centerCrop()
                .into(holder.imgEvent);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor data) {
        if (mCursor == data) return;
        Cursor oldCursor = mCursor;
        mCursor = data;
        if (data != null) {
            notifyDataSetChanged();
        }
        if (oldCursor != null) {
            oldCursor.close();
        }
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

    private void onEventSelected(int itemId, ImageView imgEvent) {
        mCursor.moveToPosition(itemId);
        int idIndex = mCursor.getColumnIndex(EventsContract.EventEntry._ID);
        mFragment.onEventSelected(mCursor.getString(idIndex), imgEvent);
    }
}
