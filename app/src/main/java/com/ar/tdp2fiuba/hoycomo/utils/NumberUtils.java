package com.ar.tdp2fiuba.hoycomo.utils;

public class NumberUtils {
    public static Integer subtractPercentage(Integer value, Integer percentage) {
        Integer valueToSubtract = percentage * value / 100;
        return value - valueToSubtract;
    }
}
