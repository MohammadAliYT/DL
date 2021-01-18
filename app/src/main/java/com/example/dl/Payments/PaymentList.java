package com.example.dl.Payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Contacts.ContactList;
import com.example.dl.Contacts.addContacts;
import com.example.dl.Databases.ContactHelperClass;
import com.example.dl.Databases.InvoiceHelperClass;
import com.example.dl.Databases.PaymentHelperClass;
import com.example.dl.Expenses.ExpenseList;
import com.example.dl.HelperClasses.ContactAdapter;
import com.example.dl.HelperClasses.PaymentAdapter;
import com.example.dl.Invoice.InvoiceList;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentList extends AppCompatActivity {

    FloatingActionButton addPaymentsFAB;

    //Variables to inflate RV
    RecyclerView paymentRecycler;
    FirebaseRecyclerOptions<PaymentHelperClass> options;
    FirebaseRecyclerAdapter<PaymentHelperClass, PaymentList.paymentViewHolder> adapter;
    DatabaseReference invoiceReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        //Hooks
        addPaymentsFAB = findViewById(R.id.addPaymentsFAB);
        paymentRecycler = findViewById(R.id.payment_recycle_view);

        //Initializing RecyclerView
        paymentRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        paymentRecycler.setHasFixedSize(true);

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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }

    public class paymentViewHolder extends RecyclerView.ViewHolder {
        public paymentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}