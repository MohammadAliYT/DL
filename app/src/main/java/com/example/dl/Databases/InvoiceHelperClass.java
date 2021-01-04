package com.example.dl.Databases;

import java.io.Serializable;

public class InvoiceHelperClass implements Serializable {
    String invoiceType, customerName, date;

    public InvoiceHelperClass() {
    }

    public InvoiceHelperClass(String invoiceType, String customerName, String date) {
        this.invoiceType = invoiceType;
        this.customerName = customerName;
        this.date = date;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
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
}
