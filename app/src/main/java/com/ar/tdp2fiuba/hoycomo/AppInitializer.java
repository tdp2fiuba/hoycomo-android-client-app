package com.ar.tdp2fiuba.hoycomo;

import android.app.Application;

import com.ar.tdp2fiuba.hoycomo.service.HttpRequestHelper;

public class AppInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpRequestHelper.initialize(getApplicationContext());
    }
}
