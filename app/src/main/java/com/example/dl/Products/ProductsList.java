package com.example.dl.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Databases.ProductHelperClass;
import com.example.dl.HelperClasses.ProductAdapter;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ProductsList extends AppCompatActivity {

    //Variables
    RecyclerView productRec;
    FloatingActionButton fabAddProducts;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fabAddProducts = findViewById(R.id.addProduct);
        productRec = findViewById(R.id.product_recycle_view);
        productRec.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ProductHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ProductHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), ProductHelperClass.class)
                        .build();

        adapter = new ProductAdapter(options);
        productRec.setAdapter(adapter);

        fabAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsList.this, addProducts.class);
                startActivity(intent);
            }
        });
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
}