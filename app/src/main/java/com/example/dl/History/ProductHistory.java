package com.example.dl.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dl.HelperClasses.CHHelperClass;
import com.example.dl.HelperClasses.PHHelperClass;
import com.example.dl.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductHistory extends AppCompatActivity {

    private RecyclerView productFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        productFirestoreList = findViewById(R.id.history_product_recycle_view);

        //FireStore Initialization
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("Products");

        FirestoreRecyclerOptions<PHHelperClass> options = new FirestoreRecyclerOptions.Builder<PHHelperClass>()
                .setQuery(query, PHHelperClass.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PHHelperClass, PHViewHolder>(options) {

            @NonNull
            @Override
            public PHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_product_history, parent, false);
                return new PHViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PHViewHolder holder, int position, @NonNull PHHelperClass model) {
                holder.name.setText(model.getName());
                holder.stock.setText("Stock " + model.getStock() + " Pcs");
                holder.customerName.setText(model.getCustomerName());
                holder.qty.setText(model.getQty() + "");
                holder.price.setText(model.getPrice() + "Rs.");
                holder.date.setText(model.getDate());
            }
        };

        productFirestoreList.setHasFixedSize(true);
        productFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        productFirestoreList.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private class PHViewHolder extends RecyclerView.ViewHolder {
        private TextView name, stock, customerName, qty, price, date;

        public PHViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameP);
            stock = itemView.findViewById(R.id.stockP);
            customerName = itemView.findViewById(R.id.customerNameP);
            qty = itemView.findViewById(R.id.productQuantityP);
            price = itemView.findViewById(R.id.productPriceP);
            date = itemView.findViewById(R.id.purchaseDateP);
        }
    }

    public void goToHistoryFromProductHistory(View view) {
        Intent intent = new Intent(ProductHistory.this, History.class);
        startActivity(intent);
    }
}