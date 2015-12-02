package com.howest.nmct.bob.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.howest.nmct.bob.R;
import com.howest.nmct.bob.api.APIAuthenticatedResponse;
import com.howest.nmct.bob.api.APILoginResponse;
import com.howest.nmct.bob.models.User;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

import static com.howest.nmct.bob.Constants.API_USER_LOGIN;
import static com.howest.nmct.bob.Constants.BACKEND_HOST;
import static com.howest.nmct.bob.Constants.BACKEND_SCHEME;
import static com.howest.nmct.bob.Constants.BACKEND_TOKEN;
import static com.howest.nmct.bob.Constants.DEVICE_MODEL;
import static com.howest.nmct.bob.Constants.DEVICE_TYPE;
import static com.howest.nmct.bob.Constants.FACEBOOK_EXPIRES;
import static com.howest.nmct.bob.Constants.FACEBOOK_PERMISSIONS;
import static com.howest.nmct.bob.Constants.FACEBOOK_TOKEN;
import static com.howest.nmct.bob.Constants.FACEBOOK_USERID;
import static com.howest.nmct.bob.Constants.USER_PROFILE;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.progress) ProgressBar progressBar;
    @Bind(R.id.errorMessage) TextView errorMessage;
    @Bind(R.id.btnTryAgain) Button btnTryAgain;

    //manage callbacks
    private CallbackManager callbackManager;
    private final OkHttpClient okHttpClient = new OkHttpClient();

    private Handler mainHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        checkSavedToken();
    }

    /**
     * Checks if there is a Facebook or a backend token saved.
     */
    private void checkSavedToken() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String fbToken = sharedPreferences.getString(FACEBOOK_TOKEN, "");
        String backendToken = sharedPreferences.getString(BACKEND_TOKEN, "");

        if (fbToken.isEmpty() && AccessToken.getCurrentAccessToken() != null ) {
            Log.i("LoginActivity", "No tokens saved - Saving token");
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            saveToken(accessToken);
            syncToken();
        } else if (backendToken.isEmpty() && !fbToken.isEmpty()) {
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
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        LoginButton btnLogin = (LoginButton) findViewById(R.id.btnLogin);
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
        String token = sharedPreferences.getString(BACKEND_TOKEN, "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(BACKEND_SCHEME)
                .encodedAuthority(BACKEND_HOST)
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
                showError("No internet connection", e);
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
     * @param accessToken The accesstoken that is to be saved
     */
    private void saveToken(AccessToken accessToken) {
        Log.i("LoginActivity", "Saving Facebook token");
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(FACEBOOK_TOKEN, accessToken.getToken())
                .putString(FACEBOOK_USERID, accessToken.getUserId())
                .putString(FACEBOOK_EXPIRES, accessToken.getExpires().toString())
                .putStringSet(FACEBOOK_PERMISSIONS, accessToken.getPermissions())
                .apply();
    }

    /**
     * Sends the facebook token to the backend and receives an user profile and backend token
     */
    private void syncToken() {
        Log.i("LoginActivity", "Syncing Facebook token");
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        RequestBody body = new FormEncodingBuilder()
                .add(FACEBOOK_TOKEN, sharedPreferences.getString(FACEBOOK_TOKEN, ""))
                .add(FACEBOOK_USERID, sharedPreferences.getString(FACEBOOK_USERID, ""))
                .add(FACEBOOK_EXPIRES, sharedPreferences.getString(FACEBOOK_EXPIRES, ""))
                .add(DEVICE_TYPE, "android")
                .add(DEVICE_MODEL, android.os.Build.MODEL)
                .build();

        Request request = new Request.Builder()
                .url(API_USER_LOGIN)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("LoginActivity", "Call failed");
                showError("No internet connection", e);
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
                .putString(BACKEND_TOKEN, apiAuthenticatedResponse.data.token)
                .apply();

        // Start the app
        onLoggedIn(apiAuthenticatedResponse.data.user);
    }

    /**
     * User has logged in. Start the next activity.
     * @param user profile
     */
    private void onLoggedIn(User user) {
        Intent i = new Intent(this, FeedActivity.class);
        i.putExtra(USER_PROFILE, user);
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

    @OnClick(R.id.btnTryAgain)
    void onTryAgainClick() {
        btnTryAgain.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        checkSavedToken();
    }

    private void showError(final String message, final IOException e) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("LoginActivity", message);
                Log.e("LoginActivity", e.getMessage());
                btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
                errorMessage = (TextView) findViewById(R.id.errorMessage);
                progressBar = (ProgressBar) findViewById(R.id.progress);

                if (btnTryAgain != null && errorMessage != null && progressBar != null) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(message);
                    btnTryAgain.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    btnTryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onTryAgainClick();
                        }
                    });
                }
            }
        });
    }

}
