package com.ar.tdp2fiuba.hoycomo.model;

public class DistanceFilter {
    private double lat;
    private double lon;
    private double distance;

    public DistanceFilter(double lat, double lon, double distance) {
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public double getDistance() {
        return this.distance;
    }
}
