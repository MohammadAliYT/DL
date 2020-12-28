package com.example.dl.Reports;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.dl.R;

public class Reports extends AppCompatActivity {

    Button customerReport,productReport,expensenprofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        customerReport = findViewById(R.id.customerReport);
        productReport = findViewById(R.id.productSalesReport);
        expensenprofit = findViewById(R.id.epBtn);

        customerReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reports.this,CustomerReport.class);
                startActivity(intent);
            }
        });

        productReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reports.this,ProductReport.class);
                startActivity(intent);
            }
        });

        expensenprofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reports.this,ExpenseReport.class);
                startActivity(intent);
            }
        });

    }
}