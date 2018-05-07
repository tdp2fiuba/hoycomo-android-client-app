package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.request.OrderRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderService extends HoyComoService {
    private static Order myOrder;

    public static void sendOrder(OrderRequest order, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String url = BASE_URL + "/order";

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try {
            JSONObject body = new JSONObject(gson.toJson(order));

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

    public static Order getMyCurrentOrder() {
        return myOrder;
    }

    public static void setAsCurrentOrder(Order order) {
        myOrder = order;
    }

    public static boolean isThereCurrentOrder() {
        return myOrder != null;
    }

    public static void clearOrder() {
        myOrder = null;
    }
}
