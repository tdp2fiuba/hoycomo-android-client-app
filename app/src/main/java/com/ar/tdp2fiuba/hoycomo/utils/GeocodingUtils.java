package com.ar.tdp2fiuba.hoycomo.utils;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class GeocodingUtils {

    public static final LatLngBounds CABABounds = new LatLngBounds(
            new LatLng(-34.691381, -58.537996),
            new LatLng(-34.530064, -58.327278)
    );

    public static String appendFloorAndApartment(String placeAddressName, @Nullable String floor, @Nullable String apartment) {
        String[] splitAddressName = placeAddressName.split(",");
        String localAddressName = splitAddressName[0];
        if (floor != null) {
            localAddressName += " " + floor;
        }
        if (apartment != null) {
            localAddressName += " " + apartment;
        }
        return localAddressName + ", " + splitAddressName[1] + ", " + splitAddressName[2];
    }

    public static String extractLocalAddress(String placeAddressName) {
        return placeAddressName.split(",")[0];
    }

}
