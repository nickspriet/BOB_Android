package com.howest.nmct.bob.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * illyism
 * 21/12/15
 */
public class EventsContract {
    public static final String CONTENT_AUTHORITY = "com.howest.nmct.bob";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENT= "event";
    public static final String PATH_PLACE = "place";

    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_UPDATED_TIME = "updated_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_COVER = "cover";
        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_ATTENDING_COUNT = "attending_count";
        public static final String COLUMN_DECLINED_COUNT = "declined_count";
        public static final String COLUMN_INTERESTED_COUNT = "interested_count";
        public static final String COLUMN_NOREPLY_COUNT = "noreply_count";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_CAN_GUESTS_INVITE = "can_guests_invite";
        public static final String COLUMN_GUEST_LIST_ENABLED = "guest_list_enabled";
        public static final String COLUMN_RSVP_STATUS = "rsvp_status";
        public static final String COLUMN_PLACE_ID = "place_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;


        public static Uri buildEventUri(String id) {
            return Uri.withAppendedPath(CONTENT_URI, id);
        }

    }

    public static final class PlaceEntry implements BaseColumns {
        public static final String TABLE_NAME = "place";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LOCATION_CITY = "city";
        public static final String COLUMN_LOCATION_COUNTRY = "country";
        public static final String COLUMN_LOCATION_LATITUDE = "latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "longitude";
        public static final String COLUMN_LOCATION_STREET = "street";
        public static final String COLUMN_LOCATION_ZIP = "zip";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;

        public static Uri buildPlaceUri(String id) {
            return Uri.withAppendedPath(CONTENT_URI, id);
        }
    }
}
