package com.example.dl.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dl.Common.LoginSignup.StartUpScreen;
import com.example.dl.Databases.SessionManager;
import com.example.dl.Expenses.ExpenseList;
import com.example.dl.Invoice.InvoiceList;
import com.example.dl.LocationOwner.Profile;
import com.example.dl.Products.ProductsList;
import com.example.dl.R;
import com.example.dl.Contacts.ContactList;
import com.example.dl.Reports.Reports;
import com.example.dl.Expenses.ExpenseList;


import java.util.HashMap;

public class UserDashboard extends AppCompatActivity {
    Button contact, products, invoices, payments, expenses, reports,history,trends;
    TextView fullNameUserText;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Hooks
        products = findViewById(R.id.products);
        contact = findViewById(R.id.contacts);
        reports = findViewById(R.id.reports);
        expenses = findViewById(R.id.expense);
        invoices = findViewById(R.id.invoices);
        fullNameUserText = findViewById(R.id.dashboardName);

        //Get all the data from Intent
        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();
        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);

        fullNameUserText.setText("" + fullName);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, ContactList.class);
                startActivity(intent);
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, ProductsList.class);
                startActivity(intent);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, Reports.class);
                startActivity(intent);
            }
        });

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, ExpenseList.class);
                startActivity(intent);
            }
        });

        invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboard.this, InvoiceList.class);
                startActivity(intent);
            }
        });
    }

    //Normal Functions
    public void callRetailerScreens(View view) {
        SessionManager sessionManager = new SessionManager(UserDashboard.this, SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin())
            startActivity(new Intent(getApplicationContext(), Profile.class));
        else
            startActivity(new Intent(getApplicationContext(), StartUpScreen.class));
    }
}