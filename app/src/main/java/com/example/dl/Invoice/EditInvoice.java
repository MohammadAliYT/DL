package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.dl.Payments.EditPayment;
import com.example.dl.Payments.PaymentList;
import com.example.dl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    //FB Variables
    DatabaseReference reference;

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
        ship = intent.getStringExtra("shippingCharges");

        uInvoiceId.setText(id);
        uCustomerName.setText(name);
        uDateText.setText(date);
        uPName.setText(pName);
        uPQty.setText(pQty);
        uPPrice.setText(pPrice);
        uSubTotal.setText(sub);
        uTotal.setText(tots);
        uShipping.setText(ship);

        //Set subtotal textView
        uPPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int price = Integer.parseInt(uPPrice.getText().toString().trim());
                int qty = Integer.parseInt(uPQty.getText().toString().trim());
                double subTotal = (price * qty);
                uSubTotal.setText(String.valueOf(subTotal));
            }
        });

        //set Total TextView
        uShipping.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //set Total TextView
                double shippingText = Double.parseDouble(uShipping.getText().toString().trim());
                double subTotalText = Double.parseDouble(uSubTotal.getText().toString().trim());
                double totalText = shippingText + subTotalText;
                uTotal.setText(String.valueOf(totalText));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void updateData() {
        if (!validateInvoiceID() | !validateCustomerName()
                | !validateProductName() | !validateProductPrice()
                | !validateProductQty() | !validateShipping()) {
            return;
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("ID", uInvoiceId.getText().toString());
        updateData.put("customerName", uCustomerName.getText().toString());
        updateData.put("date", uDateText.getText().toString());
        updateData.put("productName", uPName.getText().toString());
        updateData.put("productPrice", uPPrice.getText().toString());
        updateData.put("productQuantity", uPQty.getText().toString());
        updateData.put("subtotal", uSubTotal.getText().toString());
        updateData.put("total", uTotal.getText().toString());
        updateData.put("shippingCharges", uShipping.getText().toString());

        //Initialize FB Db
        reference = FirebaseDatabase.getInstance().getReference().child("Invoice");
        String key = getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference().child("Invoice")
                .child(key).updateChildren(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditInvoice.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditInvoice.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private boolean validateInvoiceID() {
        String val = uInvoiceId.getText().toString().trim();

        if (val.isEmpty()) {
            uInvoiceId.setError("Field can not be empty");
            return false;
        } else {
            uInvoiceId.setError(null);
            return true;
        }
    }

    private boolean validateCustomerName() {
        String val = uCustomerName.getText().toString().trim();

        if (val.isEmpty()) {
            uCustomerName.setError("Field can not be empty");
            return false;
        } else {
            uCustomerName.setError(null);
            return true;
        }
    }

    private boolean validateProductName() {
        String val = uPName.getText().toString().trim();

        if (val.isEmpty()) {
            uPName.setError("Field can not be empty");
            return false;
        } else {
            uPName.setError(null);
            return true;
        }
    }

    private boolean validateProductPrice() {
        String val = uPPrice.getText().toString().trim();

        if (val.isEmpty()) {
            uPPrice.setError("Field can not be empty");
            return false;
        } else {
            uPPrice.setError(null);
            return true;
        }
    }

    private boolean validateProductQty() {
        String val = uPQty.getText().toString().trim();

        if (val.isEmpty()) {
            uPQty.setError("Field can not be empty");
            return false;
        } else {
            uPQty.setError(null);
            return true;
        }
    }

    private boolean validateShipping() {
        String val = uShipping.getText().toString().trim();

        if (val.isEmpty()) {
            uShipping.setError("Field can not be empty");
            return false;
        } else {
            uShipping.setError(null);
            return true;
        }
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