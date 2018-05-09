package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Filter;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class StoreService extends HoyComoService {

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
}
