package com.howest.nmct.bob.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.data.Contracts.UserRideEntry;

import static com.howest.nmct.bob.data.Contracts.EventEntry;
import static com.howest.nmct.bob.data.Contracts.PlaceEntry;

/**
 * illyism
 * 21/12/15
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "bob.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createEventsTable(db);
        createRideTable(db);
        createUserRideTable(db);
        createPlaceTable(db);
        createUserTable(db);
    }

    private void createEventsTable(SQLiteDatabase db) {
        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
                EventEntry._ID + " TEXT PRIMARY KEY," +
                EventEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                EventEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                EventEntry.COLUMN_START_TIME + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_UPDATED_TIME + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_END_TIME + " INTEGER, " +
                EventEntry.COLUMN_COVER + " TEXT NOT NULL, " +
                EventEntry.COLUMN_PICTURE + " TEXT NOT NULL, " +
                EventEntry.COLUMN_ATTENDING_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_DECLINED_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_INTERESTED_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_NOREPLY_COUNT + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_OWNER + " TEXT NOT NULL, " +
                EventEntry.COLUMN_CAN_GUESTS_INVITE + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_GUEST_LIST_ENABLED + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_RSVP_STATUS + " TEXT, " +
                EventEntry.COLUMN_PLACE_ID + " TEXT, " +
                EventEntry.COLUMN_HIDE + " INTEGER DEFAULT 0, " +

                " FOREIGN KEY (" + EventEntry.COLUMN_PLACE_ID + ") REFERENCES " +
                PlaceEntry.TABLE_NAME + " (" + PlaceEntry._ID + "));";

        db.execSQL(SQL_CREATE_EVENTS_TABLE);
    }

    private void createRideTable(SQLiteDatabase db) {
        final String SQL_CREATE_RIDE_TABLE = "CREATE TABLE " + RideEntry.TABLE_NAME + " (" +
                RideEntry._ID + " TEXT PRIMARY KEY," +
                RideEntry.COLUMN_START_TIME + " INTEGER, " +
                RideEntry.COLUMN_END_TIME + " INTEGER, " +
                RideEntry.COLUMN_DESCRIPTION + " TEXT, " +
                RideEntry.COLUMN_DRIVER_ID + " TEXT NOT NULL, " +
                RideEntry.COLUMN_PLACE_ID + " TEXT, " +
                RideEntry.COLUMN_EVENT_ID + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + RideEntry.COLUMN_PLACE_ID + ") REFERENCES " +
                PlaceEntry.TABLE_NAME + " (" + PlaceEntry._ID + ") " +

                " FOREIGN KEY (" + RideEntry.COLUMN_EVENT_ID + ") REFERENCES " +
                EventEntry.TABLE_NAME + " (" + EventEntry._ID + ") " +

                " FOREIGN KEY (" + RideEntry.COLUMN_DRIVER_ID + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "));";

                db.execSQL(SQL_CREATE_RIDE_TABLE);
    }

    private void createUserRideTable(SQLiteDatabase db) {
        final String SQL_CREATE_USER_RIDE_TABLE = "CREATE TABLE " + UserRideEntry.TABLE_NAME + " (" +
                UserRideEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                UserRideEntry.COLUMN_USER_ID + " TEXT NOT NULL, " +
                UserRideEntry.COLUMN_RIDE_ID + " TEXT NOT NULL, " +
                UserRideEntry.COLUMN_STATUS + " INTEGER NOT NULL, " +

                " UNIQUE(" + UserRideEntry.COLUMN_RIDE_ID + "," + UserRideEntry.COLUMN_USER_ID + ") " +

                " FOREIGN KEY (" + UserRideEntry.COLUMN_RIDE_ID + ") REFERENCES " +
                RideEntry.TABLE_NAME + " (" + RideEntry._ID + ") " +

                " FOREIGN KEY (" + UserRideEntry.COLUMN_USER_ID + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "));";

        db.execSQL(SQL_CREATE_USER_RIDE_TABLE);
    }

    private void createPlaceTable(SQLiteDatabase db) {
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

    private void createUserTable(SQLiteDatabase db) {
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " TEXT PRIMARY KEY," +
                UserEntry.COLUMN_FACEBOOKID + " TEXT NOT NULL, " +
                UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LASTNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PICTURE + " TEXT NOT NULL, " +
                UserEntry.COLUMN_COVER + " TEXT NOT NULL, " +
                UserEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                UserEntry.COLUMN_ABOUTME + " TEXT)";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RideEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserRideEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
