package com.ar.tdp2fiuba.hoycomo.model.request;

import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.OrderItem;

import java.util.List;

public class OrderRequest {
    private String userId;
    private String storeId;
    private Integer price;
    private String description;
    private List<OrderItem> items;
    private Address address;

    public OrderRequest(String userId, String storeId, Integer price, String description, List<OrderItem> items, Address address) {
        this.userId = userId;
        this.storeId = storeId;
        this.price = price;
        this.description = description;
        this.items = items;
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

    public String getDescription() {
        return description;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "userId='" + userId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", address=" + address +
                '}';
    }
}
