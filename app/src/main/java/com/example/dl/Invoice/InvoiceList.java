package com.example.dl.Invoice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dl.Products.ProductsList;
import com.example.dl.Products.addProducts;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InvoiceList extends AppCompatActivity {

    //Variables
    FloatingActionButton fabAddInvoice;

    LinearLayout expandableView;
    Button arrowBtn;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardView = findViewById(R.id.invoiceCard);
        arrowBtn = findViewById(R.id.expand);
        expandableView = findViewById(R.id.expandable_view);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    }
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_up);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    }
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_up);
                }
            }
        });

    }

//        fabAddInvoice = findViewById(R.id.addInvoice);
//
//        fabAddInvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(InvoiceList.this, addInvoice.class);
//                startActivity(intent);
//            }
//        });


    public void goToHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }

//    public void expand(View view) {
//        if (expandableView.getVisibility() == View.GONE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                expandableView.setVisibility(View.VISIBLE);
//                expandIcon.setBackgroundResource(R.drawable.ic_up);
//
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                    expandableView.setVisibility(View.GONE);
//                    expandIcon.setBackgroundResource(R.drawable.ic_down);
//                }
//            }
//        }
//    }
}