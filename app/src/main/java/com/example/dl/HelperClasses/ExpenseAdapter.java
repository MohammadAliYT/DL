package com.example.dl.HelperClasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dl.Databases.ExpenseHelperClass;
import com.example.dl.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ExpenseAdapter extends FirebaseRecyclerAdapter<ExpenseHelperClass, ExpenseAdapter.expenseViewHolder> {

    public ExpenseAdapter(@NonNull FirebaseRecyclerOptions<ExpenseHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseAdapter.expenseViewHolder holder, int position, @NonNull ExpenseHelperClass model) {
        holder.title.setText(model.getTitle());
        holder.desc.setText("Detail: \n" + model.getDescription());
        holder.amount.setText("Expense: "+model.getAmount() +" Rs.");
        holder.date.setText(model.getDate());

        //Action for Edit Button
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus expenseDialog = DialogPlus.newDialog(holder.title.getContext())
                        .setContentHolder(new ViewHolder(R.layout.expense_dialog))
                        .setExpanded(true, 800)
                        .create();
                View productView = expenseDialog.getHolderView();
                final EditText title = productView.findViewById(R.id.ueTitle);
                final EditText desc = productView.findViewById(R.id.ueDesc);
                final EditText amount = productView.findViewById(R.id.ueAmount);
                Button saveChangesInExpense = productView.findViewById(R.id.uesubmit);

                title.setText(model.getTitle());
                desc.setText(model.getDescription());
                amount.setText(model.getAmount());

                expenseDialog.show();

                saveChangesInExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> expenseMap = new HashMap<>();
                        expenseMap.put("title", title.getText().toString());
                        expenseMap.put("desc", desc.getText().toString());
                        expenseMap.put("amount", amount.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Expenses")
                                .child(getRef(position).getKey()).updateChildren(expenseMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        expenseDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        expenseDialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        //Action For Delete Icon
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.title.getContext());
                builder.setTitle("Delete Expense");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Expenses")
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

    @NonNull
    @Override
    public expenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row, parent, false);
        return new expenseViewHolder(view);
    }

    public class expenseViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, amount, date;
        ImageView edit, delete;

        public expenseViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            desc = itemView.findViewById(R.id.descriptionText);
            amount = itemView.findViewById(R.id.amountText);
            date = itemView.findViewById(R.id.dateText);

            edit = itemView.findViewById(R.id.editicon);
            delete = itemView.findViewById(R.id.deleteicon);
        }
    }
}
