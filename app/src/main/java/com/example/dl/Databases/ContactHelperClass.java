package com.example.dl.Databases;

public class ContactHelperClass {
    String name,address,phone,balance;

    public ContactHelperClass() {
    }

    public ContactHelperClass(String name, String address, String phone, String balance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
