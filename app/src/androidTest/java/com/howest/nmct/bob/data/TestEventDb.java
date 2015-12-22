package com.howest.nmct.bob.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Date;
import java.util.HashSet;

/**
 * illyism
 * 21/12/15
 */
public class TestEventDb extends AndroidTestCase {
    public static final String LOG_TAG = TestEventDb.class.getSimpleName();

    private void deleteDatabase() {
        mContext.deleteDatabase(EventsDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        deleteDatabase();
    }

    public void testCreate() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(EventsContract.EventsEntry.TABLE_NAME);
        tableNameHashSet.add(EventsContract.PlaceEntry.TABLE_NAME);

        SQLiteDatabase db = new EventsDbHelper(mContext).getWritableDatabase();
        assertTrue(db.isOpen());

        // Check the creation of tables
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",  c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without both the place entry and event entry tables",
                tableNameHashSet.isEmpty());

        db.close();
    }

    private ContentValues placeContentValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(EventsContract.PlaceEntry._ID, "321");
        testValues.put(EventsContract.PlaceEntry.COLUMN_NAME, "Kortrijk");
        return testValues;
    }

    private ContentValues eventContentValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(EventsContract.EventsEntry._ID, "123");
        testValues.put(EventsContract.EventsEntry.COLUMN_NAME, "Test Event");
        testValues.put(EventsContract.EventsEntry.COLUMN_DESCRIPTION, "Event Description");
        testValues.put(EventsContract.EventsEntry.COLUMN_START_TIME, new Date().getTime());
        testValues.put(EventsContract.EventsEntry.COLUMN_UPDATED_TIME, new Date().getTime());
        testValues.put(EventsContract.EventsEntry.COLUMN_END_TIME, new Date().getTime());
        testValues.put(EventsContract.EventsEntry.COLUMN_COVER, "http://cover.jpg");
        testValues.put(EventsContract.EventsEntry.COLUMN_PICTURE, "http://picture.jpg");
        testValues.put(EventsContract.EventsEntry.COLUMN_ATTENDING_COUNT, 10);
        testValues.put(EventsContract.EventsEntry.COLUMN_DECLINED_COUNT, 10);
        testValues.put(EventsContract.EventsEntry.COLUMN_INTERESTED_COUNT, 10);
        testValues.put(EventsContract.EventsEntry.COLUMN_NOREPLY_COUNT, 10);
        testValues.put(EventsContract.EventsEntry.COLUMN_OWNER, "owner");
        testValues.put(EventsContract.EventsEntry.COLUMN_CAN_GUESTS_INVITE, true);
        testValues.put(EventsContract.EventsEntry.COLUMN_GUEST_LIST_ENABLED, true);
        testValues.put(EventsContract.EventsEntry.COLUMN_RSVP_STATUS, "attending");
        testValues.put(EventsContract.EventsEntry.COLUMN_PLACE_ID, "321");
        return testValues;
    }

    public void testInsert() throws Throwable {
        SQLiteDatabase db = new EventsDbHelper(mContext).getWritableDatabase();

        ContentValues eventValues = eventContentValues();
        ContentValues placeValues = placeContentValues();

        long placeRowId = db.insertOrThrow(EventsContract.PlaceEntry.TABLE_NAME, null, placeValues);
        long eventRowId = db.insertOrThrow(EventsContract.EventsEntry.TABLE_NAME, null, eventValues);

        assertTrue("Error: Failure to insert place " + placeRowId, placeRowId != -1);
        assertTrue("Error: Failure to insert event " + eventRowId, eventRowId != -1);

        db.close();
    }

    public void testQuery() throws Throwable {
        testInsert();
        SQLiteDatabase db = new EventsDbHelper(mContext).getWritableDatabase();

        Cursor c = db.query(
                EventsContract.EventsEntry.TABLE_NAME,
                new String[] {EventsContract.EventsEntry.COLUMN_NAME, EventsContract.EventsEntry.COLUMN_DESCRIPTION},
                EventsContract.EventsEntry.COLUMN_PLACE_ID + " = ?",
                new String[] {"321"},
                null, null,
                EventsContract.EventsEntry.COLUMN_ATTENDING_COUNT + " DESC",
                "1"
        );

        c.moveToFirst();
        int indexName = c.getColumnIndex(EventsContract.EventsEntry.COLUMN_NAME);
        int indexDesc = c.getColumnIndex(EventsContract.EventsEntry.COLUMN_DESCRIPTION);

        assertEquals("Test Event", c.getString(indexName));
        assertEquals("Event Description", c.getString(indexDesc));

        db.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
