package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.models.Event;

import java.util.List;

/**
 * illyism
 * 02/12/15
 */
public class APIEventsResponse extends APIResponse {
    @SerializedName("data")
    @Expose
    public APIData data;

    public APIEventsResponse(int statusCode, String message, APIData data) {
        super(statusCode, message);
        this.data = data;
    }

    public class APIData {
        @SerializedName("events")
        @Expose
        public List<Event> events;

        public APIData(List<Event> events) {
            this.events = events;
        }
    }
}
