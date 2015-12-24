package com.howest.nmct.bob.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.howest.nmct.bob.utils.PollingCheck;

import java.util.Date;

/**
 * illyism
 * 23/12/15
 * from: https://github.com/udacity/Sunshine-Version-2/blob/lesson_4_starter_code/androidTest/java/com/example/android/sunshine/app/data/TestUtilities.java
 */
public class TestUtilities extends AndroidTestCase {

    public static ContentValues getEventContentValues(String eventId, String placeId) {
        ContentValues testValues = new ContentValues();
        testValues.put(Contracts.EventEntry._ID, eventId);
        testValues.put(Contracts.EventEntry.COLUMN_NAME, "Test Event");
        testValues.put(Contracts.EventEntry.COLUMN_DESCRIPTION, "Event Description");
        testValues.put(Contracts.EventEntry.COLUMN_START_TIME, new Date().getTime());
        testValues.put(Contracts.EventEntry.COLUMN_UPDATED_TIME, new Date().getTime());
        testValues.put(Contracts.EventEntry.COLUMN_END_TIME, new Date().getTime());
        testValues.put(Contracts.EventEntry.COLUMN_COVER, "http://cover.jpg");
        testValues.put(Contracts.EventEntry.COLUMN_PICTURE, "http://picture.jpg");
        testValues.put(Contracts.EventEntry.COLUMN_ATTENDING_COUNT, 10);
        testValues.put(Contracts.EventEntry.COLUMN_DECLINED_COUNT, 10);
        testValues.put(Contracts.EventEntry.COLUMN_INTERESTED_COUNT, 10);
        testValues.put(Contracts.EventEntry.COLUMN_NOREPLY_COUNT, 10);
        testValues.put(Contracts.EventEntry.COLUMN_OWNER, "owner");
        testValues.put(Contracts.EventEntry.COLUMN_CAN_GUESTS_INVITE, true);
        testValues.put(Contracts.EventEntry.COLUMN_GUEST_LIST_ENABLED, true);
        testValues.put(Contracts.EventEntry.COLUMN_RSVP_STATUS, "attending");
        testValues.put(Contracts.EventEntry.COLUMN_PLACE_ID, placeId);
        return testValues;
    }

    public static ContentValues getPlaceContentValues(String placeId) {
        ContentValues testValues = new ContentValues();
        testValues.put(Contracts.PlaceEntry._ID, placeId);
        testValues.put(Contracts.PlaceEntry.COLUMN_NAME, "Kortrijk");
        return testValues;
    }

    public static long insertPlace(Context mContext, ContentValues placeContentValues) {
        SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
        long placeRowId = db.insert(Contracts.PlaceEntry.TABLE_NAME, null, placeContentValues);
        assertTrue("Error: Failure to insert Values", placeRowId != -1);
        return placeRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
