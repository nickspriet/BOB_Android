package com.howest.nmct.bob.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.howest.nmct.bob.Constants;
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.activities.RideDetailsActivity;
import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.fragments.RidesFragment;
import com.howest.nmct.bob.models.Event;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NextRideWidget extends AppWidgetProvider {

    private static final String[] RIDE_COLUMNS = {
            RideEntry.TABLE_NAME + "." + RideEntry._ID,
            EventEntry.TABLE_NAME + "." + EventEntry._ID,
            PlaceEntry.TABLE_NAME + "." + PlaceEntry._ID,
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_START_TIME,
            UserEntry.TABLE_NAME + "." + UserEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            EventEntry.TABLE_NAME + "." + EventEntry.COLUMN_COVER,
            PlaceEntry.TABLE_NAME + "." + EventEntry.COLUMN_NAME,
            "(SELECT count(*) FROM userride WHERE ride._id=userride.ride_id AND userride.status=1)",
            "(SELECT count(*) FROM userride WHERE ride._id=userride.ride_id AND userride.status=2)",
    };

    public static final int COL_RIDE_ID = 0;
    public static final int COL_EVENT_ID = 1;
    public static final int COL_PLACE_ID = 2;
    public static final int COL_USER_ID = 3;
    public static final int COL_RIDE_START_TIME = 4;
    public static final int COL_USER_NAME = 5;
    public static final int COL_EVENT_NAME = 6;
    public static final int COL_EVENT_COVER = 7;
    public static final int COL_PLACE_NAME = 8;
    public static final int COL_USER_RIDE_APPROVED_COUNT = 9;
    public static final int COL_USER_RIDE_REQUEST_COUNT = 10;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Picasso picasso = Picasso.with(context);
        Cursor c = context.getContentResolver().query(RideEntry.CONTENT_URI,
                RIDE_COLUMNS,
                null, null, null);

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            if (c != null && c.moveToFirst()) {
                RemoteViews views = updateAppWidget(context, appWidgetManager, appWidgetId, c);
                picasso.load(c.getString(COL_EVENT_COVER))
                        .resize(200, 200)
                        .centerCrop()
                        .into(views, R.id.ride_image, appWidgetIds);
                c.moveToNext();
            }
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private RemoteViews updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, Cursor c) {
        Date startTime = Event.parseDate(c.getString(RidesFragment.COL_RIDE_START_TIME));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
        views.setTextViewText(R.id.ride_title, c.getString(COL_EVENT_NAME));
        views.setTextViewText(R.id.location_details, c.getString(COL_PLACE_NAME));
        views.setTextViewText(R.id.shortDate, Event.formatDate("d MMM\nyyyy", startTime));

        // Create an Intent to launch RidesActivity
        Intent intent = new Intent(context, RideDetailsActivity.class);
        intent.putExtra(Constants.RIDE, c.getString(COL_RIDE_ID));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetContent, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        return views;
    }
}

