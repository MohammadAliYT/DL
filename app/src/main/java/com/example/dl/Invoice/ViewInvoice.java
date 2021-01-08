package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dl.Contacts.ContactList;
import com.example.dl.Databases.InvoiceHelperClass;
import com.example.dl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewInvoice extends AppCompatActivity {

    TextView name, id, total, date, pname, pprice, pqty, shipping, subtotal;
    ImageView delete,edit,convertToPDF;
    DatabaseReference viewInvoiceRef,deleteRef,editRef;
    InvoiceHelperClass model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        name = findViewById(R.id.invoiceNameText);
        id = findViewById(R.id.invoiceIdText);
        total = findViewById(R.id.invoiceAmountText);
        pname = findViewById(R.id.invoiceProductNameText);
        pprice = findViewById(R.id.invoicePriceText);
        pqty = findViewById(R.id.invoiceQuantityText);
        shipping = findViewById(R.id.invoiceShippingText);
        subtotal = findViewById(R.id.invoiceSubtotalText);
        date = findViewById(R.id.invoiceDateTextView);

        //Button Hooks
        delete = findViewById(R.id.invoiceDeleteBtn);
        edit=findViewById(R.id.invoiceEditBtn);

        //Initialize FB Db
        viewInvoiceRef = FirebaseDatabase.getInstance().getReference().child("Invoice");
        String key = getIntent().getStringExtra("key");
        viewInvoiceRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nameT = snapshot.child("customerName").getValue().toString();
                    name.setText(nameT);
                    String idT = snapshot.child("ID").getValue().toString();
                    id.setText("ID: " +idT);
                    String totalT = snapshot.child("total").getValue().toString();
                    total.setText("Total: " + totalT + " Rs.");
                    String pnameT = snapshot.child("productName").getValue().toString();
                    pname.setText(pnameT);
                    String ppriceT = snapshot.child("productPrice").getValue().toString();
                    pprice.setText(ppriceT+" Rs.");
                    String pqtyT = snapshot.child("productQuantity").getValue().toString();
                    pqty.setText(pqtyT +" Pcs");
                    String shippingT = snapshot.child("shippingCharges").getValue().toString();
                    shipping.setText("Shipping Charges \n" + shippingT +" Rs.");
                    String subtotalT = snapshot.child("subtotal").getValue().toString();
                    subtotal.setText("Sub-Total \n" + subtotalT +" Rs.");
                    String dateT = snapshot.child("date").getValue().toString();
                    date.setText(dateT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Delete Function
        deleteRef=FirebaseDatabase.getInstance().getReference().child("Invoice").child(key);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewInvoice.this);
                builder.setTitle("Delete Invoice");
                builder.setMessage("Delete...?");

//                FirebaseDatabase.getInstance().getReference().child("Invoice")
//                        .child(deleteRef.getKey()).removeValue();
//                startActivity(new Intent(getApplicationContext(), InvoiceList.class));
//                finish();

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Invoice")
                                .child(deleteRef.getKey()).removeValue();
                        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
                        finish();
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

        //Edit Function
        editRef=FirebaseDatabase.getInstance().getReference().child("Invoice").child(key);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewInvoice.this,EditInvoice.class);
                startActivity(intent);
            }
        });
    }

    public void goToInvoiceList(View view) {
        startActivity(new Intent(ViewInvoice.this, InvoiceList.class));
        finish();
    }

    //public void updateData(String key, InvoiceHelperClass data, final DataSta)
}