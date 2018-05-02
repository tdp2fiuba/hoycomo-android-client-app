package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class StoreService {

    private static final String BASE_URL = "https://hoycomo-server.herokuapp.com/api";

    public static void getStores(int page, int count, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/stores?page=:page&count=:count";
        final String url = urlWithPlaceholders.replace(":page", Integer.toString(page))
                .replace(":count", Integer.toString(count));

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetStores"
        );
    }

    public static void getMenu(String storeId, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        String urlWithPlaceholders = BASE_URL + "/dish/store/:storeId";
        final String url = urlWithPlaceholders.replace(":storeId", storeId);

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetMenu"
        );
    }
}
