package com.example.dl.Databases;

public class ProductHelperClass {
    String name,category,price,stock,uom;

    public ProductHelperClass() {
    }

    public ProductHelperClass(String name, String category, String price, String stock, String uom) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.uom = uom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}