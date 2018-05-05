package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_TOKEN;
import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";
    private final static CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            continueToHome();
        } else {
            setContentView(R.layout.activity_login);
            setLoginButtonListener();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setLoginButtonListener() {
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = AccessToken.getCurrentAccessToken().getToken();
                Log.d(TAG, "Facebook AccessToken: " + accessToken);

                Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final Gson gson = new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .create();
                        try {
                            final User user = gson.fromJson(response.getJSONObject("user").toString(), User.class);
                            SharedPreferencesUtils.save(getApplicationContext(), SHP_USER, user.toString());

                            final String token = response.getString("token");
                            SharedPreferencesUtils.save(getApplicationContext(), SHP_TOKEN, token);

                            Toast.makeText(LoginActivity.this, getString(R.string.welcome_username)
                                    .replace(":username", user.getFirstName() != null ? user.getFirstName() : ""), Toast.LENGTH_SHORT
                            ).show();
                            continueToHome();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error on Facebook login");
                            Toast.makeText(LoginActivity.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
                            stopLoading();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e(TAG, "Error on Facebook login");
                        Toast.makeText(LoginActivity.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
                        stopLoading();
                    }
                };
                startLoading();
                UserAuthenticationManager.logIn(accessToken, successListener, errorListener);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Log.e(TAG, "Error on Facebook login");
                Toast.makeText(LoginActivity.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void continueToHome() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void startLoading() {
        findViewById(R.id.login_container).setVisibility(View.GONE);
        findViewById(R.id.login_progress_bar).setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        findViewById(R.id.login_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.login_container).setVisibility(View.VISIBLE);
    }
}
