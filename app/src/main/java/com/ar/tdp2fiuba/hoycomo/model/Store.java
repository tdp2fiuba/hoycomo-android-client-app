package com.ar.tdp2fiuba.hoycomo.model;

public class Store {
    private String id;
    private String name;
    private String imageUrl;
    private Integer minDelayTime;
    private Integer maxDelayTime;

    public Store(String id, String name, String imageUrl, Integer minDelayTime, Integer maxDelayTime) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        if (id != null ? !id.equals(store.id) : store.id != null) return false;
        if (name != null ? !name.equals(store.name) : store.name != null) return false;
        if (imageUrl != null ? !imageUrl.equals(store.imageUrl) : store.imageUrl != null)
            return false;
        if (minDelayTime != null ? !minDelayTime.equals(store.minDelayTime) : store.minDelayTime != null)
            return false;
        return maxDelayTime != null ? maxDelayTime.equals(store.maxDelayTime) : store.maxDelayTime == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (minDelayTime != null ? minDelayTime.hashCode() : 0);
        result = 31 * result + (maxDelayTime != null ? maxDelayTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", minDelayTime=" + minDelayTime +
                ", maxDelayTime=" + maxDelayTime +
                '}';
    }
}
