package com.ar.tdp2fiuba.hoycomo.service;

import android.content.Context;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.request.FacebookLogInRequest;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.facebook.login.LoginManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_TOKEN;
import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class UserAuthenticationManager extends HoyComoService {

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

    public static void logOut(Context context) {
        LoginManager.getInstance().logOut();
        SharedPreferencesUtils.remove(context, SHP_USER);
        SharedPreferencesUtils.remove(context, SHP_TOKEN);
    }

    public static boolean isUserLoggedIn(Context context) {
        String token = SharedPreferencesUtils.load(context, SHP_TOKEN, null);
        return token != null;
    }

    public static String getToken(Context context) {
        return SharedPreferencesUtils.load(context, SHP_TOKEN, null);
    }

}
