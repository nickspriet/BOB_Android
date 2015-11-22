package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.models.User;

/**
 * illyism
 * 17/11/15
 */
public class APILoginResponse extends APIResponse {
    @SerializedName("data")
    @Expose
    public APIData data;

    public APILoginResponse(int statusCode, String message, APIData data) {
        super(statusCode, message);
        this.data = data;
    }


    public class APIData {
        @SerializedName("user")
        @Expose
        public User user;

        public APIData(User user) {
            this.user = user;
        }
    }
}
