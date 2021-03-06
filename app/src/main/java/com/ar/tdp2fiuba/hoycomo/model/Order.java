package com.ar.tdp2fiuba.hoycomo.model;

import com.ar.tdp2fiuba.hoycomo.model.request.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private User user;
    private Store store;
    private Double price;
    private Double discount;
    private List<OrderItem> items;
    private String description;
    private Address address;
    private OrderState state;
    private String registerTimestamp;

    public Order() {}

    public Order(User user, Store store, Double price, Double discount, List<OrderItem> items, Address address) {
        this(user, store, price, discount, items, null, address, new OrderState(), null);
    }

    public Order(User user, Store store, Double price, Double discount, List<OrderItem> items, String description,
                 Address address, OrderState state, String registerTimestamp) {
        this.user = user;
        this.store = store;
        this.price = price;
        this.discount = discount;
        this.items = items;
        this.description = description;
        this.address = address;
        this.state = state;
        this.registerTimestamp = registerTimestamp;
    }

    public User getUser() {
        return user;
    }

    public Store getStore() {
        return store;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void updateDiscount(Double discount) {
        this.discount = discount;
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

    public OrderState getState() {
        return state;
    }

    public String getRegisterTimestamp() {
        return registerTimestamp;
    }

    public OrderRequest toRequest() {
        return new OrderRequest(
                user.getUserId(),
                store.getId(),
                price,
                discount,
                description,
                items,
                address
        );
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", store=" + store +
                ", price=" + price +
                ", discount=" + discount +
                ", items=" + items +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", state=" + state +
                ", registerTimestamp='" + registerTimestamp + '\'' +
                '}';
    }
}
