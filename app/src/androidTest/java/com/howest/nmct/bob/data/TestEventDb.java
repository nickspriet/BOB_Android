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
        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        deleteDatabase();
    }

    public void testCreate() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(Contracts.EventEntry.TABLE_NAME);
        tableNameHashSet.add(Contracts.PlaceEntry.TABLE_NAME);

        SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
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

        c.close();
        db.close();
    }


    public void testInsert() throws Throwable {
        SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();

        ContentValues eventValues = TestUtilities.getEventContentValues("123", "321");
        ContentValues placeValues = TestUtilities.getPlaceContentValues("321");

        long placeRowId = db.insertOrThrow(Contracts.PlaceEntry.TABLE_NAME, null, placeValues);
        long eventRowId = db.insertOrThrow(Contracts.EventEntry.TABLE_NAME, null, eventValues);

        assertTrue("Error: Failure to insert place " + placeRowId, placeRowId != -1);
        assertTrue("Error: Failure to insert event " + eventRowId, eventRowId != -1);

        db.close();
    }

    public void testQuery() throws Throwable {
        testInsert();
        SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();

        Cursor c = db.query(
                Contracts.EventEntry.TABLE_NAME,
                new String[] {Contracts.EventEntry.COLUMN_NAME, Contracts.EventEntry.COLUMN_DESCRIPTION},
                Contracts.EventEntry.COLUMN_PLACE_ID + " = ?",
                new String[] {"321"},
                null, null,
                Contracts.EventEntry.COLUMN_ATTENDING_COUNT + " DESC",
                "1"
        );

        c.moveToFirst();
        int indexName = c.getColumnIndex(Contracts.EventEntry.COLUMN_NAME);
        int indexDesc = c.getColumnIndex(Contracts.EventEntry.COLUMN_DESCRIPTION);

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
