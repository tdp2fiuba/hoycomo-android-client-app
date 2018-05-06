package com.ar.tdp2fiuba.hoycomo.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String userId;
    private String storeId;
    private Integer price;
    private List<String> dishesId;
    private String description;
    private Address address;

    public Order(String userId, String storeId, Integer price, List<String> dishesId) {
        this(userId, storeId, price, dishesId, null, null);
    }

    public Order(String userId, String storeId, Integer price, List<String> dishesId, Address address) {
        this(userId, storeId, price, dishesId, null, address);
    }

    public Order(String userId, String storeId, Integer price, List<String> dishesId, String description,
                 Address address) {
        this.userId = userId;
        this.storeId = storeId;
        this.price = price;
        this.dishesId = dishesId;
        this.description = description;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer addToPrice(Integer priceToAdd) {
        this.price += priceToAdd;
        return this.price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDishesId() {
        return dishesId;
    }

    public void addDishId(String dishId) {
        if (dishesId == null) {
            dishesId = new ArrayList<>();
        }
        dishesId.add(dishId);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", dishesId=" + dishesId +
                ", address=" + address +
                '}';
    }
}
