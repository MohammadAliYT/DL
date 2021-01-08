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
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CustomerHistory extends AppCompatActivity {

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        mFirestoreList = findViewById(R.id.history_customer_recycle_view);

        //FireStore Initialization
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("History");

        FirestoreRecyclerOptions<CHHelperClass> options = new FirestoreRecyclerOptions.Builder<CHHelperClass>()
                .setQuery(query, CHHelperClass.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CHHelperClass, CHViewHolder>(options) {

            @NonNull
            @Override
            public CHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_customer_history, parent, false);
                return new CHViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CHViewHolder holder, int position, @NonNull CHHelperClass model) {
                holder.nameHitory.setText(model.getName());
                holder.balance.setText("Balance " + model.getBalance() + " Rs.");
                holder.productName.setText(model.getProductPurchased());
                holder.qty.setText(model.getQuantity() + "");
                holder.price.setText(model.getSalePrice() + "Rs.");
                holder.date.setText(model.getDate());
                holder.total.setText("Total " + model.getTotal() + " Rs.");

            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
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

    public void goToHistoryfromCustomerHistory(View view) {
        Intent intent = new Intent(CustomerHistory.this, History.class);
        startActivity(intent);
    }

    private class CHViewHolder extends RecyclerView.ViewHolder {
        private TextView nameHitory, balance, productName, qty, price, date, total;

        public CHViewHolder(@NonNull View itemView) {
            super(itemView);

            nameHitory = itemView.findViewById(R.id.nameH);
            balance = itemView.findViewById(R.id.balanceH);
            productName = itemView.findViewById(R.id.productNameH);
            qty = itemView.findViewById(R.id.productQuantityH);
            price = itemView.findViewById(R.id.productPriceH);
            date = itemView.findViewById(R.id.productDateH);
            total = itemView.findViewById(R.id.productTotalH);
        }
    }
}