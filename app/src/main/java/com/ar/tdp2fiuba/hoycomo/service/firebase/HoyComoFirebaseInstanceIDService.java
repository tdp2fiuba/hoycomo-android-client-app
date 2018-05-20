package com.ar.tdp2fiuba.hoycomo.service.firebase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import org.json.JSONObject;

public class HoyComoFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "HoyComoFirebaseIDService";

    @SuppressLint("LongLogTag")
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    Log.d("TOKEN UPDATE", "Update firebase token");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TOKEN UPDATE", "Error on update firebase token");
            }
        };

        UserAuthenticationManager.updateFirebaseToken(token, successListener, errorListener);
    }
}
