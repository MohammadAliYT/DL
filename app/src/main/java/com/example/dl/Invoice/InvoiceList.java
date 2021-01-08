package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dl.Databases.ExpenseHelperClass;
import com.example.dl.Databases.InvoiceHelperClass;
import com.example.dl.Products.ProductsList;
import com.example.dl.Products.addProducts;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InvoiceList extends AppCompatActivity {

    //Variables
    FloatingActionButton fabAddInvoice;
    RecyclerView invoiceRecycler;
    FirebaseRecyclerOptions<InvoiceHelperClass> options;
    FirebaseRecyclerAdapter<InvoiceHelperClass, invoiceViewHolder> adapter;
    DatabaseReference invoiceRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        fabAddInvoice = findViewById(R.id.addInvoice);
        invoiceRecycler = findViewById(R.id.invoice_recycle_view);

        //Initializing RecyclerView
        invoiceRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        invoiceRecycler.setHasFixedSize(true);

        fabAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvoiceList.this, addInvoice.class);
                startActivity(intent);
            }
        });

        //Initializing FB DB
        invoiceRefrence = FirebaseDatabase.getInstance().getReference().child("Invoice");

        LoadData();
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<InvoiceHelperClass>().setQuery(invoiceRefrence, InvoiceHelperClass.class).build();
        adapter = new FirebaseRecyclerAdapter<InvoiceHelperClass, invoiceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull invoiceViewHolder holder, int position, @NonNull InvoiceHelperClass model) {
                holder.invoiceName.setText(model.getCustomerName());
                holder.invoiceDate.setText(model.getDate());
                holder.invoiceID.setText("ID: " + model.getID());
                holder.invoiceAmount.setText(model.getTotal() + " Rs.");

                //Action for Details Button
                holder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InvoiceList.this, ViewInvoice.class);
                        intent.putExtra("key", getRef(position).getKey());
                        startActivity(intent);
                    }
                });

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditInvoice.class);
                        intent.putExtra("ID", model.getID());
                        intent.putExtra("customerName", model.getCustomerName());
                        intent.putExtra("date", model.getDate());
//                      intent.putExtra("invoiceType", model.getInvoiceType());
                        intent.putExtra("productName", model.getProductName());
                        intent.putExtra("productPrice", model.getProductPrice());
                        intent.putExtra("productQuantity", model.getProductQuantity());
                        intent.putExtra("shippingCharges", model.getShippingCharges());
                        intent.putExtra("subtotal", model.getSubtotal());
                        intent.putExtra("total", model.getTotal());
                        view.getContext().startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public invoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice_list, parent, false);
                return new invoiceViewHolder(view);
            }
        };

        adapter.startListening();
        invoiceRecycler.setAdapter(adapter);
    }

    private class invoiceViewHolder extends RecyclerView.ViewHolder {
        private TextView invoiceName, invoiceDate, invoiceAmount, invoiceID;
        private Button details, edit;
        private String key;
        View v;

        public invoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            details = itemView.findViewById(R.id.detailBtn);
            invoiceName = itemView.findViewById(R.id.nameOnInvoice);
            invoiceDate = itemView.findViewById(R.id.invoiceDateView);
            invoiceAmount = itemView.findViewById(R.id.invoiceAmountView);
            invoiceID = itemView.findViewById(R.id.invoiceIdTextView);
            edit = itemView.findViewById(R.id.editBtn);
            v = itemView;
        }
    }
}