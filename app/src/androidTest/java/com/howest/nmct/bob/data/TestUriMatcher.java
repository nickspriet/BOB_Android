package com.howest.nmct.bob.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {
    private static final String EVENT_ID = "123";

    private static final Uri TEST_EVENT_DIR = Contracts.EventEntry.CONTENT_URI;
    private static final Uri TEST_EVENT_ID = Contracts.EventEntry.buildEventUri(EVENT_ID);
    private static final Uri TEST_PLACE_DIR = Contracts.PlaceEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = Provider.buildUriMatcher();

        assertEquals("Error: The EVENT URI was matched incorrectly.",
                testMatcher.match(TEST_EVENT_DIR), Provider.EVENT);
        assertEquals("Error: The EVENT ID URI was matched incorrectly.",
                testMatcher.match(TEST_EVENT_ID), Provider.EVENT_ID);
        assertEquals("Error: The PLACE URI was matched incorrectly.",
                testMatcher.match(TEST_PLACE_DIR), Provider.PLACE);
    }
}