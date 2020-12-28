package com.example.dl.Expenses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dl.Contacts.ContactList;
import com.example.dl.HelperClasses.DatePickerFragment;
import com.example.dl.Products.ProductsList;
import com.example.dl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addExpense extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextInputLayout title,amount;
    EditText desc;
    Button saveButton,calenderBtn;
    TextView dateViewExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Expense Form Fields Initialization
        desc  = findViewById(R.id.description);
        title = findViewById(R.id.titleExpense);
        amount = findViewById(R.id.amountExpense);
        saveButton = findViewById(R.id.saveBtn);

        //Date
        dateViewExpenses = findViewById(R.id.dateTextViewExpense);
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        dateViewExpenses.setText(date_n);

        //Open Calendar
        calenderBtn = findViewById(R.id.calendarButton);
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        
        //Action to add Data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        Map<String, Object> expenseMap = new HashMap<>();
        expenseMap.put("title",title.getEditText().getText().toString());
        expenseMap.put("amount",amount.getEditText().getText().toString());
        expenseMap.put("description",desc.getText().toString().trim());
        expenseMap.put("date",dateViewExpenses.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Expenses").push()
                .setValue(expenseMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        title.getEditText().setText("");
                        amount.getEditText().setText("");
                        desc.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ExpenseList.class));
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

    public void goToExpenseList(View view) {
        startActivity(new Intent(getApplicationContext(), ExpenseList.class));
        finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_WEEK, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = findViewById(R.id.dateTextViewExpense);
        textView.setText(currentDateString);
    }
}