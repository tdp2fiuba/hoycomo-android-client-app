package com.ar.tdp2fiuba.hoycomo.model;

import com.google.firebase.iid.FirebaseInstanceId;

public class FacebookLogInRequest {
    private String accessToken;
    private String firebaseToken;

    public FacebookLogInRequest(String accessToken) {
        this.accessToken = accessToken;
        this.firebaseToken = FirebaseInstanceId.getInstance().getToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getFirebaseId() {
        return firebaseToken;
    }
}
