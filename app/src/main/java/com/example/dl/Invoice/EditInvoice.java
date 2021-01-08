package com.example.dl.Invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.dl.HelperClasses.DatePickerFragment;
import com.example.dl.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditInvoice extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    //Variables
    private Spinner uIT;
    private EditText uInvoiceId, uCustomerName, uShipping;
    private TextView uDateText, uSubTotal, uTotal;
    private EditText uPName, uPQty, uPPrice;
    private LinearLayout updatedLayout;
    private Button ubtnAdd, update, updateCalenderBtnInvoice;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> invoiceSpinnerData;
    private ImageView removeBtn;
    private View add_products;
    String id, name, type, date, pName, pPrice, pQty, sub, ship, tots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invoice);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        uIT = findViewById(R.id.updateInvoiceTypeSpinner);
        uInvoiceId = findViewById(R.id.updateIdText);
        uCustomerName = findViewById(R.id.updateCustomerText);
        uShipping = findViewById(R.id.updateShippingAmount);
        uDateText = findViewById(R.id.updateDateTextInvoice);
        uSubTotal = findViewById(R.id.updateSubTotal);
        uTotal = findViewById(R.id.updateTotal);
        updatedLayout = findViewById(R.id.productAddList);
        ubtnAdd = findViewById(R.id.updateButtonAdd);
        update = findViewById(R.id.updateInvoice);
        updateCalenderBtnInvoice = findViewById(R.id.calendarBtnInvoice);

        //Dynamic Layout
        add_products = getLayoutInflater().inflate(R.layout.row_add_product_in_invoice, null, false);

        //Viewing Dynamic Layout
        uPName = add_products.findViewById(R.id.pname);
        uPQty = add_products.findViewById(R.id.pqty);
        uPPrice = add_products.findViewById(R.id.pprice);
        removeBtn = add_products.findViewById(R.id.premovebtn);

        //Populating spinner with customer name
        invoiceSpinnerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(EditInvoice.this, android.R.layout.simple_spinner_dropdown_item, invoiceSpinnerData);

        //Populating Spinner
        ArrayAdapter<CharSequence> invoiceType = ArrayAdapter.createFromResource(this, R.array.invoice_type, android.R.layout.simple_spinner_item);
        invoiceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uIT.setAdapter(invoiceType);
        uIT.setOnItemSelectedListener(this);

        //Date
        String date_invoice = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        uDateText.setText(date_invoice);

        //Open Calendar
        updateCalenderBtnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment uDatePickerInvoice = new DatePickerFragment();
                uDatePickerInvoice.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        ubtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });


        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("customerName");
        date = intent.getStringExtra("date");
        pName = intent.getStringExtra("productName");
        pPrice = intent.getStringExtra("productPrice");
        pQty = intent.getStringExtra("productQuantity");
        sub = intent.getStringExtra("subtotal");
        tots = intent.getStringExtra("total");

        uInvoiceId.setText(id);
        uCustomerName.setText(name);
        uDateText.setText(date);
        uPName.setText(pName);
        uPQty.setText(pQty);
        uPPrice.setText(pPrice);
        uSubTotal.setText(sub);
        uTotal.setText(tots);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("Invoice");
                String uid, uname, utype, udate, upName, upPrice, upQty, usub, uship, utots;
                uid = uInvoiceId.getText().toString();
                uname = uCustomerName.getText().toString();
                utype = uIT.getSelectedItem().toString();
                udate = uDateText.getText().toString();
                upName = uPName.getText().toString();
                upPrice = uPPrice.getText().toString();
                upQty = uPQty.getText().toString();
                usub = uSubTotal.getText().toString();
                uship = uShipping.getText().toString();
                utots = uTotal.getText().toString();


                InvoiceHelperClass invoiceHelperClass = new InvoiceHelperClass(id, uname, utype, udate, upName, upPrice, upQty, usub, uship, utots);
                updateRef.setValue(invoiceHelperClass);
                Toast.makeText(EditInvoice.this, "Data Updates", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addView() {
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(add_products);
            }
        });
        updatedLayout.addView(add_products);
    }

    private void removeView(View view) {
        updatedLayout.removeView(view);
    }

    public void goToInvoiceList(View view) {
        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
        finish();
    }

    //Method To Bind Calendar
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_WEEK, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = findViewById(R.id.updateDateTextInvoice);
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
}