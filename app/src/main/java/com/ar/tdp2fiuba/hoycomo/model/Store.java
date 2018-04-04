package com.ar.tdp2fiuba.hoycomo.model;

public class Store {
    private String id;
    private String name;
    private String imageUrl;
    private Integer minDelayTime;
    private Integer maxDelayTime;
    private Availability availability;

    @Deprecated
    public Store(String id, String name, String imageUrl, Integer minDelayTime, Integer maxDelayTime) {
        this(id, name, imageUrl, minDelayTime, maxDelayTime, null);
    }

    public Store(String id, String name, String imageUrl, Integer minDelayTime, Integer maxDelayTime,
                 Availability availability) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getMinDelayTime() {
        return minDelayTime;
    }

    public Integer getMaxDelayTime() {
        return maxDelayTime;
    }

    public Availability getAvailability() {
        return availability != null ?
                availability :
                new Availability(       // Available 24/7
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00"),
                        new DailyTimeWindow("00", "00")
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        return id != null ? id.equals(store.id) : store.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", minDelayTime=" + minDelayTime +
                ", maxDelayTime=" + maxDelayTime +
                ", availability=" + availability +
                '}';
    }
}
