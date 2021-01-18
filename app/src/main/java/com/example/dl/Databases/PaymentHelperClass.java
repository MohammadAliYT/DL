package com.example.dl.Databases;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PaymentHelperClass {
    String amount, date, invoiceID;

    public PaymentHelperClass() {

    }

    public PaymentHelperClass(String amount, String date, String invoiceID) {
        this.amount = amount;
        this.date = date;
        this.invoiceID = invoiceID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

}
