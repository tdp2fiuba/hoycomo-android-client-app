package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.ar.tdp2fiuba.hoycomo.model.Review;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StoreService extends HoyComoService {

    public static void getStore(String storeId, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/api/store/:storeId";
        final String url = urlWithPlaceholders.replace(":storeId", storeId);

        HttpRequestHelper.get(
                url,
                null,
                successListener,
                errorListener,
                "GetStore"
        );
    }

    public static void getStores(int page, int count, Filter filter, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/api/stores?page=:page&count=:count&filters=:filters";
        final String url = urlWithPlaceholders.replace(":page", Integer.toString(page))
                .replace(":count", Integer.toString(count))
                .replace(":filters", filter == null ? "null" : filter.parseToJSONString());

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetStores"
        );
    }

    public static void getMenu(String storeId, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String urlWithPlaceholders = BASE_URL + "/api/dish/store/:storeId";
        final String url = urlWithPlaceholders.replace(":storeId", storeId);

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetMenu"
        );
    }

    public static void getReviews(String storeId, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String urlWithPlaceholders = BASE_URL + "/api/store/:storeId/reviews";
        final String url = urlWithPlaceholders.replace(":storeId", storeId);

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetReviews"
        );
    }

    public static void postReview(String storeId, Review review, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        String urlWithPlaceholders = BASE_URL + "/api/store/:storeId/reviews";
        final String url = urlWithPlaceholders.replace(":storeId", storeId);

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try {
            JSONObject body = new JSONObject(gson.toJson(review));

            HttpRequestHelper.post(
                    url,
                    null,
                    body,
                    successListener,
                    errorListener,
                    "PostReview"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
