package com.example.dl.HelperClasses;

public class ProductModel {
    private String name;
    private long price;

    public ProductModel() {
    }

    public ProductModel(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
