package com.howest.nmct.bob.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.howest.nmct.bob.R;
import com.howest.nmct.bob.collections.Rides;
import com.howest.nmct.bob.interfaces.RidesLoadedListener;
import com.howest.nmct.bob.models.Ride;
import com.howest.nmct.bob.models.User;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashSet;

/**
 * Implementation of App Widget functionality.
 */
public class NextRideWidget extends AppWidgetProvider {
    private Ride mRide;
    final User mProfile = new User("1", "1", "Ilias Ismanalijev", "Ilias", "Ismanalijev",
            "https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xap1/v/t1.0-9/12027626_1077838225560816_4235616874323113303_n.jpg?oh=e0e858a8876f3a49b69bc0690064fc27&oe=56B28D3B&__gda__=1454655490_c13993560b66647cf06de663af6b237c",
            "https://scontent-bru2-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/11846638_1053922741285698_465322780535143622_n.jpg?oh=f34b22f77aa08411af8da81729063ba6&oe=56B5A67B", "" +
            "https://www.facebook.com/IliasIsmanalijev", "about");

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
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


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_ride_widget);
        views.setTextViewText(R.id.ride_title, mRide.event.getName());
        views.setTextViewText(R.id.location_details, mRide.getAddress());
        views.setTextViewText(R.id.approval_status, Ride.formatApprovalStatus(mRide, mProfile));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

