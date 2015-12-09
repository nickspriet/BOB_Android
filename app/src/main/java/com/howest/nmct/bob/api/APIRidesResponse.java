package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.models.Ride;

import java.util.LinkedHashSet;

/**
 * illyism
 * 02/12/15
 */
public class APIRidesResponse extends APIResponse {
    @SerializedName("data")
    @Expose
    public APIData data;

    public APIRidesResponse(int statusCode, String message, APIData data) {
        super(statusCode, message);
        this.data = data;
    }

    public class APIData {
        @SerializedName("rides")
        @Expose
        public LinkedHashSet<Ride> rides;

        public APIData(LinkedHashSet<Ride> events) {
            this.rides = events;
        }
    }
}
