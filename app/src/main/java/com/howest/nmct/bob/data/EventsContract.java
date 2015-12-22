package com.howest.nmct.bob.data;

import android.provider.BaseColumns;

/**
 * illyism
 * 21/12/15
 */
public class EventsContract {

    public static final class EventsEntry implements BaseColumns {
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
    }
}
