package com.howest.nmct.bob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.howest.nmct.bob.api.APIAuthenticatedResponse;
import com.howest.nmct.bob.api.APILoginResponse;
import com.howest.nmct.bob.models.User;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private LoginButton btnLogin;

    //manage callbacks
    private CallbackManager callbackManager;
    OkHttpClient okHttpClient = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSavedToken();
    }

    /**
     * Checks if there is a Facebook or a backend token saved.
     */
    private void checkSavedToken() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String fbToken = sharedPreferences.getString(Constants.FACEBOOK_TOKEN, "");
        String backendToken = sharedPreferences.getString(Constants.BACKEND_TOKEN, "");

        if (backendToken.isEmpty() && !fbToken.isEmpty()) {
            Log.i("LoginActivity", "No Backend Token - Attempting login");
            setContentView(R.layout.activity_loading);
            syncToken();
        } else if (!backendToken.isEmpty() && !fbToken.isEmpty()) {
            Log.i("LoginActivity", "We have a backend token - Get profile");
            setContentView(R.layout.activity_loading);
            getProfile();
        } else {
            Log.i("LoginActivity", "No backend token and no facebook token");
            initLoginButton();
        }
    }

    /**
     * Open up the correct view and show the login button
     */
    private void initLoginButton() {
        //the SDK needs to be initialized before using any of its methods
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        btnLogin = (LoginButton) findViewById(R.id.btnLogin);
        btnLogin.setReadPermissions(
                "public_profile", // For showing your name, cover photo, profile pictures, ...
                "email", // For sending you notification emails
                "user_friends", // For helping you invite friends to share a ride
                "user_events", // For helping you find events
                "user_likes", // For helping your find users with similar interests
                "user_location" // For helping you find other members from your area
        );

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                saveToken(accessToken);
                syncToken();
            }

            @Override
            public void onCancel() {
                Log.i("LoginActivity", "Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginActivity", "Login attempt failed.");
            }
        });
    }

    /**
     * User already has a token from our backend.
     * Do a call to the backend to get a profile.
     */
    private void getProfile() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.BACKEND_TOKEN, "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.BACKEND_SCHEME)
                .encodedAuthority(Constants.BACKEND_HOST)
                .appendPath("user")
                .appendPath("profile")
                .appendQueryParameter("token", token);

        Request request = new Request.Builder()
                .url(builder.build().toString())
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("LoginActivity", "Call failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("LoginActivity", responseString);
                APILoginResponse apiResponse = new Gson().fromJson(responseString, APILoginResponse.class);
                onLoggedIn(apiResponse.data.user);
            }
        });
    }

    /**
     * Save the Facebook token in our SharedPreferences
     * @param accessToken
     */
    private void saveToken(AccessToken accessToken) {
        Log.i("LoginActivity", "Saving Facebook token");
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(Constants.FACEBOOK_TOKEN, accessToken.getToken())
                .putString(Constants.FACEBOOK_USERID, accessToken.getUserId())
                .putString(Constants.FACEBOOK_EXPIRES, accessToken.getExpires().toString())
                .putStringSet(Constants.FACEBOOK_PERMISSIONS, accessToken.getPermissions())
                .apply();
    }

    /**
     * Sends the facebook token to the backend and receives an user profile and backend token
     */
    private void syncToken() {
        Log.i("LoginActivity", "Syncing Facebook token");
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        RequestBody body = new FormEncodingBuilder()
                .add(Constants.FACEBOOK_TOKEN, sharedPreferences.getString(Constants.FACEBOOK_TOKEN, ""))
                .add(Constants.FACEBOOK_USERID, sharedPreferences.getString(Constants.FACEBOOK_USERID, ""))
                .add(Constants.FACEBOOK_EXPIRES, sharedPreferences.getString(Constants.FACEBOOK_EXPIRES, ""))
                .add(Constants.DEVICE_TYPE, "android")
                .add(Constants.DEVICE_MODEL, android.os.Build.MODEL)
                .build();

        Request request = new Request.Builder()
                .url(Constants.API_USER_LOGIN)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("LoginActivity", "Call failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                Log.i("LoginActivity", responseString);
                APIAuthenticatedResponse apiResponse = new Gson().fromJson(responseString, APIAuthenticatedResponse.class);
                onAuthenticated(apiResponse);
            }
        });
    }

    /**
     * Received a token from our backend as well as user information
     */
    private void onAuthenticated(APIAuthenticatedResponse apiAuthenticatedResponse) {
        Log.i("LoggedIn", apiAuthenticatedResponse.data.user.name);

        // Save the backend token
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(Constants.BACKEND_TOKEN, apiAuthenticatedResponse.data.token)
                .apply();

        // Start the app
        onLoggedIn(apiAuthenticatedResponse.data.user);
    }

    /**
     * User has logged in. Start the next activity.
     * @param user profile
     */
    private void onLoggedIn(User user) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Constants.USER_PROFILE, user);
        startActivity(i);
        finish();
    }

    //handle the result received from the activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


}
