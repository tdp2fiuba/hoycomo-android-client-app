package com.ar.tdp2fiuba.hoycomo.model;

import java.util.List;

public class MenuItem {
    private String id;
    private String storeId;
    private String name;
    private Integer price;
    private Integer discount;
    private List<String> pictures;

    public MenuItem(String id, String storeId, String name, Integer price, Integer discount, List<String> pictures) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.pictures = pictures;
    }

    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public List<String> getPictures() {
        return pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;

        return id != null ? id.equals(menuItem.id) : menuItem.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id='" + id + '\'' +
                ", storeId='" + storeId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", pictures=" + pictures +
                '}';
    }
}
