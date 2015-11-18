package com.howest.nmct.bob.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.models.User;

/**
 * illyism
 * 17/11/15
 */
public class APIAuthenticatedResponse extends APILoginResponse {
    @SerializedName("data")
    @Expose
    public APIData data;


    public APIAuthenticatedResponse(int statusCode, String message, User user, APIData data) {
        super(statusCode, message, user, data);
    }

    public class APIData extends APILoginResponse.APIData {
        @SerializedName("token")
        @Expose
        public String token;

        public APIData(String token, User user) {
            super(user);
            this.token = token;
        }
    }
}
