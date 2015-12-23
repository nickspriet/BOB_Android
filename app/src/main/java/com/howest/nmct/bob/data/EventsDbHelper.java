package com.howest.nmct.bob.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.howest.nmct.bob.data.EventsContract.EventEntry;
import static com.howest.nmct.bob.data.EventsContract.PlaceEntry;

/**
 * illyism
 * 21/12/15
 */
public class EventsDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "events.db";

    public EventsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
                EventEntry._ID + " TEXT PRIMARY KEY," +
                EventsContract.EventEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                EventsContract.EventEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                EventEntry.COLUMN_START_TIME + " INTEGER NOT NULL, " +
                EventsContract.EventEntry.COLUMN_UPDATED_TIME + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_END_TIME + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_COVER + " TEXT NOT NULL, " +
                EventEntry.COLUMN_PICTURE + " TEXT NOT NULL, " +
                EventEntry.COLUMN_ATTENDING_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_DECLINED_COUNT + " INTEGER NOT NULL, " +
                EventsContract.EventEntry.COLUMN_INTERESTED_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_NOREPLY_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_OWNER + " TEXT NOT NULL, " +
                EventEntry.COLUMN_CAN_GUESTS_INVITE + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_GUEST_LIST_ENABLED + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_RSVP_STATUS + " TEXT NOT NULL, " +
                EventEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + EventEntry.COLUMN_PLACE_ID + ") REFERENCES " +
                PlaceEntry.TABLE_NAME + " (" + PlaceEntry._ID + "));";

        db.execSQL(SQL_CREATE_EVENTS_TABLE);

        final String SQL_CREATE_PLACE_TABLE = "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +
                PlaceEntry._ID +  " TEXT PRIMARY KEY, " +
                PlaceEntry.COLUMN_NAME + " TEXT, " +
                PlaceEntry.COLUMN_LOCATION_STREET + " TEXT, " +
                PlaceEntry.COLUMN_LOCATION_ZIP + " TEXT, " +
                PlaceEntry.COLUMN_LOCATION_CITY + " TEXT, " +
                PlaceEntry.COLUMN_LOCATION_COUNTRY + " TEXT, " +
                PlaceEntry.COLUMN_LOCATION_LONGITUDE + " REAL, " +
                PlaceEntry.COLUMN_LOCATION_LATITUDE + " REAL)";

        db.execSQL(SQL_CREATE_PLACE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);
        onCreate(db);
    }
}
