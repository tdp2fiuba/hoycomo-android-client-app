package com.ar.tdp2fiuba.hoycomo.model;

import com.google.gson.Gson;

import java.util.List;

public class Filter {
    private DistanceFilter distance;
    private String[] foodTypes;
    private Integer delayTime;
    private Float rating;
    private Double averagePrice;

    public Filter() {}

    public Filter(DistanceFilter distance) {
        this.distance = distance;
    }

    public DistanceFilter getDistanceFilter() {
        return this.distance;
    }

    public void setDistanceFilter(Double lat, Double lon, Double distance) {
        this.distance = new DistanceFilter(lat, lon, distance);
    }

    public String[] getFoodTypes() {
        return this.foodTypes;
    }

    public void setFoodTypes(String[] foodTypes) {
        this.foodTypes = foodTypes;
    }

    public Integer getDelayTime() {
        return this.delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Float getRating() {
        return this.rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Double getAveragePrice() {
        return this.averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setDistanceFilter(DistanceFilter distanceFilter) {
        this.distance = distanceFilter;
    }

    public String parseToJSONString() {
        return new Gson().toJson(this);
    }

    public boolean isEmpty() {
        return this.distance == null && this.rating == null && this.averagePrice == null && this.delayTime == null && this.foodTypes == null;
    }

    public static Filter parseJSONFilter(String filterJSON) {
        return new Gson().fromJson(filterJSON, Filter.class);
    }
}
