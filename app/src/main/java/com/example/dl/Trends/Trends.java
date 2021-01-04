package com.example.dl.Trends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.dl.Contacts.ContactList;
import com.example.dl.R;
import com.example.dl.Reports.CustomerReport;
import com.example.dl.Reports.Reports;
import com.example.dl.User.UserDashboard;

public class Trends extends AppCompatActivity {

    Button customerTrend,productTrend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        customerTrend = findViewById(R.id.customerTrend);
        productTrend = findViewById(R.id.productTrend);

        customerTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trends.this, CustomerTrend.class);
                startActivity(intent);
            }
        });

        productTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trends.this, ProductTrend.class);
                startActivity(intent);
            }
        });

    }

    public void goToHomeFromTrends(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }
}