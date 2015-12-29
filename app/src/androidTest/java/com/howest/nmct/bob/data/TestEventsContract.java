package com.howest.nmct.bob.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * illyism
 * 22/12/15
 */
public class TestEventsContract extends AndroidTestCase {
    private static final String TEST_EVENT_ID = "123";

    public void testBuildUri() {
        Uri eventUri = Contracts.EventEntry.buildEventUri(TEST_EVENT_ID);
        assertNotNull("Error: Null Uri returned.",  eventUri);
        assertEquals("Error: Event id not properly appended to the end of the Uri",
                TEST_EVENT_ID, eventUri.getLastPathSegment());
        assertEquals("Error: Event Uri doesn't match our expected result",
                "content://com.howest.nmct.bob/event/123", eventUri.toString());
    }
}