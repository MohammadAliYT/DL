package com.example.dl.Databases;

import java.io.Serializable;

public class InvoiceHelperClass implements Serializable {
    private String ID, customerName, date, invoiceType, productName, productPrice, productQuantity, shippingCharges, subtotal, total;

    public InvoiceHelperClass() {
    }

    public InvoiceHelperClass(String ID, String customerName, String date, String invoiceType, String productName, String productPrice, String productQuantity, String shippingCharges, String subtotal, String total) {
        this.ID = ID;
        this.customerName = customerName;
        this.date = date;
        this.invoiceType = invoiceType;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.shippingCharges = shippingCharges;
        this.subtotal = subtotal;
        this.total = total;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
