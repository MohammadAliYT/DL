package com.example.dl.Expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Contacts.ContactList;
import com.example.dl.Contacts.addContacts;
import com.example.dl.Databases.ContactHelperClass;
import com.example.dl.Databases.ExpenseHelperClass;
import com.example.dl.HelperClasses.ContactAdapter;
import com.example.dl.HelperClasses.ExpenseAdapter;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ExpenseList extends AppCompatActivity {

    //Variables
    RecyclerView expenseRV;
    FloatingActionButton fabAddExpense;
    ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        expenseRV = findViewById(R.id.expense_recycle_view);
        fabAddExpense = findViewById(R.id.addExpense);
        expenseRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ExpenseHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ExpenseHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Expenses"), ExpenseHelperClass.class)
                        .build();

        adapter= new ExpenseAdapter(options);
        expenseRV.setAdapter(adapter);

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseList.this, addExpense.class);
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