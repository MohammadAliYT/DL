package com.example.dl.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dl.Contacts.ContactList;
import com.example.dl.Databases.ProductHelperClass;
import com.example.dl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addProducts extends AppCompatActivity {

    //Variables
    TextInputLayout name, category, price, stock,uomText;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        name = findViewById(R.id.product_name);
        category = findViewById(R.id.product_category);
        price = findViewById(R.id.product_purchase);
        stock = findViewById(R.id.product_stock);
        save = findViewById(R.id.saveProduct);
        uomText = findViewById(R.id.product_uom);

        //Save Button Action
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });

    }

    private void processinsert() {

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("name", name.getEditText().getText().toString());
        productMap.put("category", category.getEditText().getText().toString());
        productMap.put("price", price.getEditText().getText().toString());
        productMap.put("stock", price.getEditText().getText().toString());
        productMap.put("uom", uomText.getEditText().getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Products").push()
                .setValue(productMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.getEditText().setText("");
                        category.getEditText().setText("");
                        price.getEditText().setText("");
                        stock.getEditText().setText("");
                        uomText.getEditText().setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ProductsList.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                    }
                });

    }


    public void goToProductList(View view) {
        startActivity(new Intent(getApplicationContext(), ProductsList.class));
        finish();
    }

}