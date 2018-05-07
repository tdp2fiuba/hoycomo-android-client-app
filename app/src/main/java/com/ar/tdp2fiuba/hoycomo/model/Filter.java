package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

public class Filter {
    private DistanceFilter distance;

    public Filter() {}

    public Filter(DistanceFilter distance) {
        this.distance = distance;
    }

    public DistanceFilter getDistanceFilter() {
        return this.distance;
    }

    public void setDistanceFilter(double lat, double lon, double distance) {
        this.distance = new DistanceFilter(lat, lon, distance);
    }

    public void setDistanceFilter(DistanceFilter distanceFilter) {
        this.distance = distanceFilter;
    }

    public String parseToJSONString() {
        return new Gson().toJson(this);
    }

    public static Filter parseJSONFilter(String filterJSON) {
        Filter filter = new Gson().fromJson(filterJSON, Filter.class);
        return filter;
    }
}
