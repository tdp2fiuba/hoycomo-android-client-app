package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Filter;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class StoreService {

    private static final String BASE_URL = "https://hoycomo-server.herokuapp.com/api";

    public static void getStores(int page, int count, Filter filter, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/stores?page=:page&count=:count&filters=:filters";
        String url = urlWithPlaceholders.replace(":page", Integer.toString(page))
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
}
