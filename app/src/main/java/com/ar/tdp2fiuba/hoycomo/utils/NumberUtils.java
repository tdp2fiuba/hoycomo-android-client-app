package com.ar.tdp2fiuba.hoycomo.utils;

public class NumberUtils {
    public static Double subtractPercentage(Double value, Double percentage) {
        Double valueToSubtract = percentage * value / 100;
        return value - valueToSubtract;
    }
}
