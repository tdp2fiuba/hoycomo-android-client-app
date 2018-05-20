package com.ar.tdp2fiuba.hoycomo.service;

import com.android.volley.Response;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.request.OrderRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderService extends HoyComoService {
    private static Order myOrder;

    public static void sendOrder(OrderRequest order, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String url = BASE_URL + "/api/order";

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
                    "SendOrder"
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getActiveOrders(String userId, Response.Listener<JSONArray> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/api/order/user/:userId?state=TAKEN&state=PREPARATION&state=DISPATCHED";
        final String url = urlWithPlaceholders.replace(":userId", userId);

        HttpRequestHelper.getArray(
                url,
                null,
                successListener,
                errorListener,
                "GetActiveOrders"
        );
    }

    public static void rejectOrder(String orderId, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        final String urlWithPlaceholders = BASE_URL + "/api/order/:orderId/reject";
        final String url = urlWithPlaceholders.replace(":orderId", orderId);

        HttpRequestHelper.post(
                url,
                null,
                null,
                successListener,
                errorListener,
                "GetActiveOrders"
        );
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
