package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.models.Ride;

/**
 * illyism
 * 16/12/15
 */
public class APIRideResponse extends APIResponse {
    @SerializedName("data")
    @Expose
    public APIData data;

    public APIRideResponse(int statusCode, String message, APIData data) {
        super(statusCode, message);
        this.data = data;
    }

    public class APIData {
        @SerializedName("ride")
        @Expose
        public Ride ride;

        public APIData(Ride ride) {
            this.ride = ride;
        }
    }
}