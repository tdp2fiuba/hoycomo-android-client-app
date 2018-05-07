package com.ar.tdp2fiuba.hoycomo.model;


import android.text.TextUtils;

import java.util.List;

public class Store {
    private String id;
    private String name;
    private Address address;
    private String avatar;
    private DelayTime delayTime;
    private Availability availability;
    private List<String> foodTypes;

    public Store(String id, String name, Address address, String avatar, DelayTime delayTime,
                 Availability availability) {
        this(id, name, address, avatar, delayTime, availability, null);
    }

    public Store(String id, String name, Address address, String avatar, DelayTime delayTime,
                 Availability availability, List<String> foodTypes) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.avatar = avatar;
        this.delayTime = delayTime;
        this.availability = availability;
        this.foodTypes = foodTypes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public DelayTime getDelayTime() {
        return delayTime;
    }

    public Availability getAvailability() {
        return availability != null ?
                availability :
                new Availability(       // Available 24/7
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24"),
                        new DailyTimeWindow("00", "24")
                );
    }

    public String getParsedFoodTypesAsString() {
        if (foodTypes == null) {
            return "";
        }
        return TextUtils.join(", ", foodTypes);
    }

    public List<String> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(List<String> value) {
        this.foodTypes = value;
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
                ", address=" + address +
                ", avatar='" + avatar + '\'' +
                ", delayTime=" + delayTime +
                ", availability=" + availability +
                ", foodTypes='" + getFoodTypes() + '\'' +
                '}';
    }
}
