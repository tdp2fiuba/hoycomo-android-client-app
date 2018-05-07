package com.ar.tdp2fiuba.hoycomo.model;

import com.ar.tdp2fiuba.hoycomo.model.request.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String userId;
    private Store store;
    private Integer price;
    private List<OrderItem> items;
    private String description;
    private Address address;

    public Order(String userId, Store store, Integer price, List<OrderItem> items) {
        this(userId, store, price, items, null, null);
    }

    public Order(String userId, Store store, Integer price, List<OrderItem> items, Address address) {
        this(userId, store, price, items, null, address);
    }

    public Order(String userId, Store store, Integer price, List<OrderItem> items, String description,
                 Address address) {
        this.userId = userId;
        this.store = store;
        this.price = price;
        this.items = items;
        this.description = description;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public Store getStore() {
        return store;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        price += item.getPrice() * item.getQuantity();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OrderRequest toRequest() {
        return new OrderRequest(
                userId,
                store.getId(),
                price,
                description,
                items,
                address
        );
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", store='" + store + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", address=" + address +
                '}';
    }
}
