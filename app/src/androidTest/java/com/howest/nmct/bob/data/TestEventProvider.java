package com.howest.nmct.bob.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.howest.nmct.bob.data.Contracts.EventEntry;
import com.howest.nmct.bob.data.Contracts.PlaceEntry;

/**
 * illyism
 * 22/12/15
 */
public class TestEventProvider extends AndroidTestCase {
    private static final String LOG_TAG = TestEventProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                EventEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                PlaceEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Event table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Place table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(EventEntry.TABLE_NAME, null, null);
        db.delete(PlaceEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromDB();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // EventsProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                Provider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: EventsProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + Contracts.CONTENT_AUTHORITY,
                    providerInfo.authority, Contracts.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: EventsProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        String type = getContext().getContentResolver().getType(EventEntry.CONTENT_URI);
        assertEquals("Error: the EventEntry CONTENT_URI should return EventEntry.CONTENT_TYPE",
                EventEntry.CONTENT_TYPE, type);

        type = getContext().getContentResolver().getType(PlaceEntry.CONTENT_URI);
        assertEquals("Error: the PlaceEntry CONTENT_URI should return PlaceEntry.CONTENT_TYPE",
                PlaceEntry.CONTENT_TYPE, type);

        type = getContext().getContentResolver().getType(EventEntry.buildEventUri("123"));
        assertEquals("Error: this should return EventEntry.CONTENT_ITEM_TYPE",
                EventEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testEventQuery() {
         // insert our test records into the database
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues placeContentValues = TestUtilities.getPlaceContentValues("321");
        long placeRowId = TestUtilities.insertPlace(mContext, placeContentValues);

        ContentValues eventContentValues = TestUtilities.getEventContentValues("123", Long.toString(placeRowId));

        long eventRowId = db.insert(EventEntry.TABLE_NAME, null, eventContentValues);
        assertTrue("Unable to Insert EventEntry into the Database", eventRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor c = mContext.getContentResolver().query(
                EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertTrue("Empty cursor returned", c.moveToFirst());
        int indexName = c.getColumnIndex(EventEntry.COLUMN_NAME);
        int indexDesc = c.getColumnIndex(EventEntry.COLUMN_DESCRIPTION);

        assertEquals("Test Event", c.getString(indexName));
        assertEquals("Event Description", c.getString(indexDesc));
    }

    public void testEventUpdate() {
        final String PLACE_ID = "321";
        ContentValues placeContentValues = TestUtilities.getPlaceContentValues(PLACE_ID);

        Uri placeUri = mContext.getContentResolver().
                insert(PlaceEntry.CONTENT_URI, placeContentValues);
        long placeRowId = ContentUris.parseId(placeUri);

        // Verify we got a row back.
        assertTrue(placeRowId != -1);

        ContentValues updatedValues = new ContentValues(placeContentValues);
        updatedValues.put(PlaceEntry.COLUMN_NAME, "Test Place");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor placeCursor = mContext.getContentResolver().query(PlaceEntry.CONTENT_URI, null, null, null, null);

        assertTrue("Empty cursor returned", placeCursor.moveToFirst());

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        placeCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                PlaceEntry.CONTENT_URI, updatedValues, PlaceEntry._ID + "= ?",
                new String[] { PLACE_ID });
        assertEquals(1, count);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        tco.waitForNotificationOrFail();

        placeCursor.unregisterContentObserver(tco);
        placeCursor.close();

        Cursor c = mContext.getContentResolver().query(
                PlaceEntry.CONTENT_URI,
                null,   // projection
                PlaceEntry._ID + " = " + PLACE_ID,
                null,   // Values for the "where" clause
                null    // sort order
        );

        assertTrue("Empty cursor returned", c.moveToFirst());
        int indexName = c.getColumnIndex(EventEntry.COLUMN_NAME);

        assertEquals("Test Place", c.getString(indexName));
        assertNotSame("Kortrijk", c.getString(indexName));

        c.close();
    }
}