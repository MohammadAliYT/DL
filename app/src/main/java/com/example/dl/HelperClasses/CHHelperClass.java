package com.example.dl.HelperClasses;

public class CHHelperClass {
    private String name;
    private String date;
    private String productPurchased;
    private long total;
    private long balance;
    private long quantity;
    private long salePrice;

    private CHHelperClass(String name, String date, String productPurchased, long total, long balance, long quantity, long salePrice) {
        this.name = name;
        this.date = date;
        this.productPurchased = productPurchased;
        this.total = total;
        this.balance = balance;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    private CHHelperClass() {
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

    public String getProductPurchased() {
        return productPurchased;
    }

    public void setProductPurchased(String productPurchased) {
        this.productPurchased = productPurchased;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(long salePrice) {
        this.salePrice = salePrice;
    }
}