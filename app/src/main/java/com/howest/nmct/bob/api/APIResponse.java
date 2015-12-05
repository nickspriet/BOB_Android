package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * illyism
 * 17/11/15
 */
public class APIResponse {
    @SerializedName("statusCode")
    @Expose
    public int statusCode;
    @SerializedName("message")
    @Expose
    public String message;

    APIResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
