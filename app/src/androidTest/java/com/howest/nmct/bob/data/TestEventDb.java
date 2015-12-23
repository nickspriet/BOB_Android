package com.howest.nmct.bob.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

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
        tableNameHashSet.add(EventsContract.EventEntry.TABLE_NAME);
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


    public void testInsert() throws Throwable {
        SQLiteDatabase db = new EventsDbHelper(mContext).getWritableDatabase();

        ContentValues eventValues = TestUtilities.getEventContentValues("123", "321");
        ContentValues placeValues = TestUtilities.getPlaceContentValues("321");

        long placeRowId = db.insertOrThrow(EventsContract.PlaceEntry.TABLE_NAME, null, placeValues);
        long eventRowId = db.insertOrThrow(EventsContract.EventEntry.TABLE_NAME, null, eventValues);

        assertTrue("Error: Failure to insert place " + placeRowId, placeRowId != -1);
        assertTrue("Error: Failure to insert event " + eventRowId, eventRowId != -1);

        db.close();
    }

    public void testQuery() throws Throwable {
        testInsert();
        SQLiteDatabase db = new EventsDbHelper(mContext).getWritableDatabase();

        Cursor c = db.query(
                EventsContract.EventEntry.TABLE_NAME,
                new String[] {EventsContract.EventEntry.COLUMN_NAME, EventsContract.EventEntry.COLUMN_DESCRIPTION},
                EventsContract.EventEntry.COLUMN_PLACE_ID + " = ?",
                new String[] {"321"},
                null, null,
                EventsContract.EventEntry.COLUMN_ATTENDING_COUNT + " DESC",
                "1"
        );

        c.moveToFirst();
        int indexName = c.getColumnIndex(EventsContract.EventEntry.COLUMN_NAME);
        int indexDesc = c.getColumnIndex(EventsContract.EventEntry.COLUMN_DESCRIPTION);

        assertEquals("Test Event", c.getString(indexName));
        assertEquals("Event Description", c.getString(indexDesc));

        c.close();
        db.close();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
