package com.howest.nmct.bob.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.models.Ride;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class NextRideWidget extends AppWidgetProvider {
    Ride mRide;
    // User mProfile = new User("1", "Ilias Ismanalijev");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Rides.fetchData();
        mRide = Rides.getRides().get(0);

        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
        Picasso picasso = Picasso.with(context);
        picasso.load(mRide.getImage())
                .resize(200, 200)
                .centerCrop()
                .into(views, R.id.ride_image, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
        views.setTextViewText(R.id.ride_title, mRide.getTitle());
        views.setTextViewText(R.id.location_details, mRide.getAddress());
        // views.setTextViewText(R.id.approval_status, Ride.formatApprovalStatus(mRide, mProfile));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

