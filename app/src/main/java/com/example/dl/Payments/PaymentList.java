package com.example.dl.Payments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Contacts.ContactList;
import com.example.dl.Contacts.addContacts;
import com.example.dl.Expenses.ExpenseList;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaymentList extends AppCompatActivity {

    FloatingActionButton addPaymentsFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        //Hooks
        addPaymentsFAB = findViewById(R.id.addPaymentsFAB);

        //FAB Action
        addPaymentsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentList.this, addPayment.class);
                startActivity(intent);
            }
        });

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }
}