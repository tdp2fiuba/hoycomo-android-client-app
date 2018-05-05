package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Filter;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class StoreService {

    private static final String BASE_URL = "https://hoycomo-server.herokuapp.com/api";

    public static void getStores(int page, int count, Filter filter, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String url = BASE_URL + "/stores";
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("page", Integer.toString(page));
        requestParams.put("count", Integer.toString(count));
        if (filter != null) {
            requestParams.put("filters", filter.parseToJSONString());
        }

        HttpRequestHelper.getArray(
                url,
                requestParams,
                successListener,
                errorListener,
                "GetStores"
        );
    }
}
