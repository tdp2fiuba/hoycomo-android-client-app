package com.ar.tdp2fiuba.hoycomo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtils {

    private static final String MY_PREFS_NAME = "hoy_como_prefs";

    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String load(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }
}
