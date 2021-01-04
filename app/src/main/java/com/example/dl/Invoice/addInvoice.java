package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dl.Databases.InvoiceHelperClass;
import com.example.dl.Expenses.ExpenseList;
import com.example.dl.HelperClasses.DatePickerFragment;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addInvoice extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    //Variables
    LinearLayout linearLayout;
    Button btnAdd, save, calenderBtnInvoice;
    Spinner invoiceSpinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> invoiceSpinnerData;
    TextView dateViewInvoice;
    EditText customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);

        //Hooks
        linearLayout = findViewById(R.id.productAddList);
        btnAdd = findViewById(R.id.buttonAdd);
        dateViewInvoice = findViewById(R.id.dateTextInvoice);
        invoiceSpinner = findViewById(R.id.invoiceTypeSpinner);
        customer = findViewById(R.id.customerText);
        calenderBtnInvoice = findViewById(R.id.calendarBtnInvoice);
        save = findViewById(R.id.saveInvoice);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Populating spinner with customer name
        invoiceSpinnerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(addInvoice.this, android.R.layout.simple_spinner_dropdown_item, invoiceSpinnerData);

        //Populating Spinner
        ArrayAdapter<CharSequence> invoiceType = ArrayAdapter.createFromResource(this, R.array.invoice_type, android.R.layout.simple_spinner_item);
        invoiceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        invoiceSpinner.setAdapter(invoiceType);
        invoiceSpinner.setOnItemSelectedListener(this);

        //Date
        String date_invoice = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        dateViewInvoice.setText(date_invoice);

        //Open Calendar
        calenderBtnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickerInvoice = new DatePickerFragment();
                datePickerInvoice.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInvoices();
            }
        });


    }

    private void saveInvoices() {
        Map<String, Object> invoiceMap = new HashMap<>();
        invoiceMap.put("invoiceType", invoiceSpinner.getSelectedItem().toString());
        invoiceMap.put("customerName", customer.getText().toString().trim());
        invoiceMap.put("date", dateViewInvoice.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Invoice").push()
                .setValue(invoiceMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customer.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
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

    //Method To Bind Calendar
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_WEEK, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = findViewById(R.id.dateTextInvoice);
        textView.setText(currentDateString);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Invoice Type", Toast.LENGTH_SHORT).show();
        } else {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void addView() {

        final View add_products = getLayoutInflater().inflate(R.layout.row_add_product_in_invoice, null, false);
        EditText pname = add_products.findViewById(R.id.pname);
        EditText pqty = add_products.findViewById(R.id.pqty);
        EditText pprice = add_products.findViewById(R.id.pprice);
        ImageView removeBtn = add_products.findViewById(R.id.premovebtn);

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(add_products);
            }
        });

        linearLayout.addView(add_products);
    }

    private void removeView(View view) {
        linearLayout.removeView(view);
    }

    public void goToInvoiceList(View view) {
        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
        finish();
    }
}