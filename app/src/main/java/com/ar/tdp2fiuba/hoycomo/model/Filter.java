package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

public class Filter {
    private DistanceFilter distanceFilter;

    public Filter() {}

    public Filter(DistanceFilter distanceFilter) {
        this.distanceFilter = distanceFilter;
    }

    public DistanceFilter getDistanceFilter() {
        return this.distanceFilter;
    }

    public void setDistanceFilter(double lat, double lon, double distance) {
        this.distanceFilter = new DistanceFilter(lat, lon, distance);
    }

    public String parseToJSONString() {
        return new Gson().toJson(this);
    }

    public static Filter parseJSONFilter(String filterJSON) {
        Filter filter = new Gson().fromJson(filterJSON, Filter.class);
        return filter;
    }
}
