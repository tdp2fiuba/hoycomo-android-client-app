package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;

import org.json.JSONArray;

/**
 * Created by bliberini on 30/5/18.
 */

public class FoodTypeService extends HoyComoService {
    public static void getAllFoodTypes(Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String url = BASE_URL + "/api/foodTypes/";
        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetFoodTypes"
        );
    }
}
