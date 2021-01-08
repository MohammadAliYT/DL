package com.example.dl.HelperClasses;

public class PHHelperClass {
    private String name;
    private String date;
    private String customerName;
    private long price;
    private long stock;
    private long qty;

    public PHHelperClass(String name, String date, String customerName, long price, long stock, long qty) {
        this.name = name;
        this.date = date;
        this.customerName = customerName;
        this.price = price;
        this.stock = stock;
        this.qty = qty;
    }

    public PHHelperClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
}