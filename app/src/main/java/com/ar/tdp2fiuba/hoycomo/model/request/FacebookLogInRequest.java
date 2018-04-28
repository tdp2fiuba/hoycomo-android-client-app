package com.ar.tdp2fiuba.hoycomo.model.request;

public class FacebookLogInRequest {
    private String accessToken;

    public FacebookLogInRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
