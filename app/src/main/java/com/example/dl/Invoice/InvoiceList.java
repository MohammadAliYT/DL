package com.example.dl.Invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Products.ProductsList;
import com.example.dl.Products.addProducts;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InvoiceList extends AppCompatActivity {

    //Variables
    FloatingActionButton fabAddInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fabAddInvoice = findViewById(R.id.addInvoice);

        fabAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvoiceList.this, addInvoice.class);
                startActivity(intent);
            }
        });
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }
}