package com.example.dl.HelperClasses;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dl.Databases.ContactHelperClass;
import com.example.dl.Databases.PaymentHelperClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PaymentAdapter extends FirebaseRecyclerAdapter<PaymentHelperClass, PaymentAdapter.myviewholder> {

    public PaymentAdapter(@NonNull FirebaseRecyclerOptions<PaymentHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PaymentHelperClass model) {

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        public myviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
