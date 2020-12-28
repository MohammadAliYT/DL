package com.example.dl.HelperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dl.Databases.ProductHelperClass;
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

public class ProductAdapter extends FirebaseRecyclerAdapter<ProductHelperClass, ProductAdapter.productViewHolder> {

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<ProductHelperClass> options) {
        super(options);
    }

    //holder.spinner.setOnTouchListener(spinnerOnTouch);
    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.productViewHolder holder, int position, @NonNull ProductHelperClass model) {
        holder.productName.setText("" + model.getName());
        holder.productPrice.setText("Price: " + model.getPrice() +" Rs.");

        //Action for Edit Button
        holder.productEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus productDialog = DialogPlus.newDialog(holder.productName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.product_dialog))
                        .setExpanded(true, 800)
                        .create();

                View productView = productDialog.getHolderView();
                final EditText name = productView.findViewById(R.id.upname);
                final EditText category = productView.findViewById(R.id.upcategory);
                final EditText price = productView.findViewById(R.id.upprice);
                final EditText stock = productView.findViewById(R.id.upstock);
                final EditText uom = productView.findViewById(R.id.upUOM);
                Button saveChanges = productView.findViewById(R.id.upsubmit);

                name.setText(model.getName());
                category.setText(model.getCategory());
                price.setText(model.getPrice());
                stock.setText(model.getStock());
                uom.setText(model.getUom());

                productDialog.show();

                saveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("category", category.getText().toString());
                        map.put("price", price.getText().toString());
                        map.put("stock", stock.getText().toString());
                        map.put("uom", uom.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Products")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        productDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        productDialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        //Action For Delete Icon
        holder.productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.productName.getContext());
                builder.setTitle("Delete Product");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Products")
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
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        return new productViewHolder(view);
    }


    public class productViewHolder extends RecyclerView.ViewHolder {
        ImageView productEdit, productDelete;
        TextView  productName, productPrice;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameText);
            productPrice = itemView.findViewById(R.id.priceText);

            productEdit = itemView.findViewById(R.id.editiconProduct);
            productDelete = itemView.findViewById(R.id.deleteiconProduct);
        }
    }
}