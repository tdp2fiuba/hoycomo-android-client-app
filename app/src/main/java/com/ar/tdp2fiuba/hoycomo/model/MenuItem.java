package com.ar.tdp2fiuba.hoycomo.model;

import java.util.List;

public class MenuItem {
    private String id;
    private String storeId;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<String> pictures;
    private List<String> garnishes;
    private boolean celiac;
    private boolean diabetic;
    private boolean vegan;
    private boolean vegetarian;

    @Deprecated
    public MenuItem() {}

    public MenuItem(String id, String storeId, String name, String description, Double price, Double discount,
                    List<String> pictures, List<String> garnishes,
                    boolean celiac, boolean diabetic, boolean vegan, boolean vegetarian) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.pictures = pictures;
        this.garnishes = garnishes;
        this.celiac = celiac;
        this.diabetic = diabetic;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
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

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public List<String> getGarnishes() {
        return garnishes;
    }

    public boolean isCeliac() {
        return celiac;
    }

    public boolean isDiabetic() {
        return diabetic;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
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
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", pictures=" + pictures +
                ", garnishes=" + garnishes +
                ", celiac=" + celiac +
                ", diabetic=" + diabetic +
                ", vegan=" + vegan +
                ", vegetarian=" + vegetarian +
                '}';
    }
}
