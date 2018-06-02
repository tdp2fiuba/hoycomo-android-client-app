package com.ar.tdp2fiuba.hoycomo.model;

public class DistanceFilter {
    private Double lat;
    private Double lon;
    private Double distance;

    public DistanceFilter(Double lat, Double lon, Double distance) {
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
    }

    public DistanceFilter() {

    }

    public Double getLat() {
        return this.lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
