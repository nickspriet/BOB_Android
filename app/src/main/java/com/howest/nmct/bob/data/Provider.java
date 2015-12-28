package com.howest.nmct.bob.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;
import com.howest.nmct.bob.data.Contracts.RideEntry;
import com.howest.nmct.bob.data.Contracts.UserEntry;
import com.howest.nmct.bob.data.Contracts.UserRideEntry;

/**
 * illyism
 * 22/12/15
 */
public class Provider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    static final int EVENT = 100;
    static final int EVENT_ID = 101;
    static final int RIDE = 200;
    static final int RIDE_ID = 201;
    static final int USER_RIDE = 250;
    static final int USER_RIDE_ID = 251;
    static final int PLACE = 300;
    static final int PLACE_ID = 301;
    static final int USER = 400;
    static final int USER_ID = 401;

    static final String sEventWithPlace = EventEntry.TABLE_NAME + " LEFT OUTER JOIN " +
            PlaceEntry.TABLE_NAME +
            " ON " + EventEntry.COLUMN_PLACE_ID +
            " = " + PlaceEntry.TABLE_NAME + "." + PlaceEntry._ID;

    static final String sRideWithUserEventPlace = RideEntry.TABLE_NAME +
            " LEFT OUTER JOIN " +
                EventEntry.TABLE_NAME +
                " ON " + RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_EVENT_ID +
                " = " + EventEntry.TABLE_NAME + "." + EventEntry._ID +
            " LEFT OUTER JOIN " +
                UserEntry.TABLE_NAME +
                " ON " + RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_DRIVER_ID +
                " = " + UserEntry.TABLE_NAME + "." + UserEntry._ID +
            " LEFT OUTER JOIN " +
                PlaceEntry.TABLE_NAME +
                " ON " + RideEntry.TABLE_NAME + "." + RideEntry.COLUMN_PLACE_ID +
                " = " + PlaceEntry.TABLE_NAME + "." + PlaceEntry._ID;

    static final String sUserRideWithUser = UserRideEntry.TABLE_NAME +
            " LEFT OUTER JOIN " +
                UserEntry.TABLE_NAME +
                " ON " + UserRideEntry.TABLE_NAME + "." + UserRideEntry.COLUMN_USER_ID +
                " = " + UserEntry.TABLE_NAME + "." + UserEntry._ID;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_EVENT, EVENT);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_EVENT + "/*", EVENT_ID);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_RIDE, RIDE);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_RIDE + "/*", RIDE_ID);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_USER_RIDE, USER_RIDE);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_USER_RIDE + "/*", USER_RIDE_ID);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_PLACE, PLACE);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_PLACE + "/*", PLACE_ID);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_USER, USER);
        uriMatcher.addURI(Contracts.CONTENT_AUTHORITY, Contracts.PATH_USER + "/*", USER_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENT:
                return EventEntry.CONTENT_TYPE;
            case EVENT_ID:
                return EventEntry.CONTENT_ITEM_TYPE;
            case RIDE:
                return RideEntry.CONTENT_TYPE;
            case RIDE_ID:
                return RideEntry.CONTENT_ITEM_TYPE;
            case USER_RIDE:
                return UserRideEntry.CONTENT_TYPE;
            case USER_RIDE_ID:
                return UserRideEntry.CONTENT_ITEM_TYPE;
            case PLACE:
                return PlaceEntry.CONTENT_TYPE;
            case PLACE_ID:
                return PlaceEntry.CONTENT_ITEM_TYPE;
            case USER:
                return UserEntry.CONTENT_TYPE;
            case USER_ID:
                return UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "event/*"
            case EVENT_ID:
            {
                final String eventId = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        sEventWithPlace,
                        projection,
                        "(" + EventEntry.TABLE_NAME + "." + EventEntry._ID + "=?)",
                        new String[] { eventId },
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "ride/*"
            case RIDE_ID:
            {
                final String rideId = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        sRideWithUserEventPlace,
                        projection,
                        "(" + RideEntry.TABLE_NAME + "." + RideEntry._ID + "=?)",
                        new String[] {rideId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "userride"
            case USER_RIDE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        sUserRideWithUser,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "userride/*"
            case USER_RIDE_ID: {
                final String rideId = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        UserRideEntry.TABLE_NAME,
                        projection,
                        "(" + UserRideEntry.COLUMN_RIDE_ID + "=?)",
                        new String[] {rideId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "place"
            case PLACE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PlaceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "event"
            case EVENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        sEventWithPlace,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "ride"
            case RIDE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        sRideWithUserEventPlace,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "user"
            case USER_ID: {
                final String userId = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        UserEntry.TABLE_NAME,
                        projection,
                        "(" + UserEntry._ID + "=?)",
                        new String[] { userId },
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case EVENT: {
                long _id = db.insert(EventEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = EventEntry.buildEventUri(Long.toString(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RIDE: {
                long _id = db.insert(RideEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = RideEntry.buildRideUri(Long.toString(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PLACE: {
                long _id = db.insert(PlaceEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = PlaceEntry.buildPlaceUri(Long.toString(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER: {
                long _id = db.insertWithOnConflict(UserEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if ( _id > 0 )
                    returnUri = UserEntry.buildUserUri(Long.toString(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;

        // also return amount of rows deleted on null
        if (null == selection) selection = "1";
        switch (match) {
            case EVENT:
                deletedRows = db.delete(
                        EventEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case RIDE:
                deletedRows = db.delete(
                        RideEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case PLACE:
                deletedRows = db.delete(
                        PlaceEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case USER:
                deletedRows = db.delete(
                        UserEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRows = 0;

        switch (match) {
            case EVENT:
                updatedRows = db.update(
                        EventEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case RIDE:
                updatedRows = db.update(
                        RideEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case RIDE_ID:
                final String rideId = uri.getPathSegments().get(1);
                updatedRows = db.update(
                        RideEntry.TABLE_NAME,
                        values,
                        "(" + RideEntry._ID + " =?)",
                        new String[] {rideId}
                );
                break;
            case PLACE:
                updatedRows = db.update(
                        PlaceEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case USER:
                updatedRows = db.update(
                        UserEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case USER_ID:
                final String userId = uri.getPathSegments().get(1);
                updatedRows = db.update(
                        UserEntry.TABLE_NAME,
                        values,
                        "(" + UserEntry._ID + " = ?)",
                        new String[] {userId}
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case EVENT:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(EventEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case RIDE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(RideEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case USER_RIDE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(UserRideEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case PLACE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(PlaceEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
