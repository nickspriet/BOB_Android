package com.howest.nmct.bob.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Implementation of App Widget functionality.
 */
public class NextRideWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        /*
        Rides.fetchData(context, new RidesLoadedListener() {
            @Override
            public void ridesLoaded(LinkedHashSet<Ride> rides) {
                mRide = Rides.getRides().iterator().next();

                // final int N = appWidgetIds.length;
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
                Picasso picasso = Picasso.with(context);
                if (!mRide.event.getCover().isEmpty()) {
                    picasso.load(mRide.event.getCover())
                            .resize(200, 200)
                            .centerCrop()
                            .into(views, R.id.ride_image, appWidgetIds);
                }
            }
        });
        */
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

            /*
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
        views.setTextViewText(R.id.ride_title, mRide.event.getName());
        views.setTextViewText(R.id.location_details, mRide.getAddress());
        views.setTextViewText(R.id.approval_status, Ride.formatApprovalStatus(mRide, mProfile));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        */
    }
}

