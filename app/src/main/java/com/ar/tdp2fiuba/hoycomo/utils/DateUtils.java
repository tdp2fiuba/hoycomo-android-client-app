package com.ar.tdp2fiuba.hoycomo.utils;

public class DateUtils {
    public static String formatTime(String time) {
        String[] units = time.split(":");
        return units[1].equals("00") ? units[0] : units[0] + ":" + units[1];
    }
}
