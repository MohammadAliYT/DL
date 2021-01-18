package com.example.dl.Payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.dl.Databases.PaymentHelperClass;
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
    DatabaseReference paymentReference,paymentDeleteReference;

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
        //Initializing FB DB
        paymentReference = FirebaseDatabase.getInstance().getReference().child("Payments");

        LoadPaymentData();
    }

    private void LoadPaymentData() {
        options = new FirebaseRecyclerOptions.Builder<PaymentHelperClass>().setQuery(paymentReference, PaymentHelperClass.class).build();
        adapter = new FirebaseRecyclerAdapter<PaymentHelperClass, paymentViewHolder>(options) {
            @NonNull
            @Override
            public paymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_list, parent, false);
                return new paymentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull paymentViewHolder holder, int position, @NonNull PaymentHelperClass model) {
                holder.id.setText("Invoice ID:\n" + model.getInvoiceID());
                holder.amount.setText("Rs: " + model.getAmount());
                holder.date.setText("Date:" + model.getDate());

                //Edit Button Action
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),EditPayment.class);
                        intent.putExtra("key", getRef(position).getKey());
                        intent.putExtra("invoiceID", model.getInvoiceID());
                        intent.putExtra("amount", model.getAmount());
                        intent.putExtra("date", model.getDate());
                        view.getContext().startActivity(intent);
                    }
                });

                //Delete Button Action
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentList.this);
                        builder.setTitle("Delete Payment");
                        builder.setMessage("Delete...?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference().child("Payments")
                                        .child(getRef(position).getKey()).removeValue();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();
                    }
                });
            }
        };

        adapter.startListening();
        paymentRecycler.setAdapter(adapter);
    }


    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }

    private class paymentViewHolder extends RecyclerView.ViewHolder {
        TextView date, amount, id;
        Button edit, delete;

        public paymentViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.paymentDateView);
            amount = itemView.findViewById(R.id.paymentAmountView);
            id = itemView.findViewById(R.id.idofInvoice);
            edit = itemView.findViewById(R.id.editBtnPayment);
            delete = itemView.findViewById(R.id.deleteBtnPayment);
        }
    }
}