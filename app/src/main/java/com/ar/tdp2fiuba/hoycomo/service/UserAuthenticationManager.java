package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.request.FacebookLogInRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationManager {

    private static final String BASE_URL = "https://hoycomo-server.herokuapp.com/api";

    public static void logIn(String accessToken, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String url = BASE_URL + "/user/auth/facebook";

        FacebookLogInRequest requestModel = new FacebookLogInRequest(accessToken);
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try {
            JSONObject body = new JSONObject(gson.toJson(requestModel));

            HttpRequestHelper.post(
                    url,
                    null,
                    body,
                    successListener,
                    errorListener,
                    "LogIn"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
