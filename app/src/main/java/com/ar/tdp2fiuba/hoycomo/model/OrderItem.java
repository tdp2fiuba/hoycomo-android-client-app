package com.ar.tdp2fiuba.hoycomo.model;

public class OrderItem {
    private String id;
    private String name;
    private Integer price;
    private Integer quantity;
    private String garnish;
    private String comments;

    public OrderItem(String id, String name, Integer price) {
        this(id, name, price, 1);
    }

    public OrderItem(String id, String name, Integer price, Integer quantity) {
        this(id, name, price, quantity, null, null);
    }

    public OrderItem(String id, String name, Integer price, Integer quantity, String garnish, String comments) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.garnish = garnish;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getGarnish() {
        return garnish;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        return id != null ? id.equals(orderItem.id) : orderItem.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", garnish='" + garnish + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
