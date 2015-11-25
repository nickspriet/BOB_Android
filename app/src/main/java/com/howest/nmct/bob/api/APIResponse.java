package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * illyism
 * 17/11/15
 */
class APIResponse {
    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    APIResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
