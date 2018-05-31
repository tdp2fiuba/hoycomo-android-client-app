package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

import java.util.List;

public class Filter {
    private DistanceFilter distance;
    private String[] foodTypes;
    private int delayTime;
    private double rating;
    private double averagePrice;

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

    public String[] getFoodTypes() {
        return this.foodTypes;
    }

    public void setFoodTypes(String[] foodTypes) {
        this.foodTypes = foodTypes;
    }

    public int getDelayTime() {
        return this.delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAveragePrice() {
        return this.averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
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
