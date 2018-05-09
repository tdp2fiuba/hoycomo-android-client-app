package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;

import org.json.JSONObject;

public class GeocodingService extends HoyComoService {
    public static void validateAddress(String address, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/utils/geocoding?address_name=:address";
        final String url = urlWithPlaceholders.replace(":address", address);

        HttpRequestHelper.get(
                url,
                null,
                successListener,
                errorListener,
                "ValidateAddress"
        );
    }
}
