package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
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
    EditText pname, pqty, pprice;
    ImageView removeBtn;
    View add_products;
    TextView subtotal, total;
    EditText shipping;
    EditText invoiceId;

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
        subtotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        shipping = findViewById(R.id.shippingAmount);
        invoiceId = findViewById(R.id.idText);


        //set Total TextView
        shipping.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //set Total TextView
                double shippingText = Double.parseDouble(shipping.getText().toString().trim());
                double subTotalText = Double.parseDouble(subtotal.getText().toString().trim());
                double totalText = shippingText + subTotalText;
                total.setText(String.valueOf(totalText));
            }
        });

        //Dynamic Layout
        add_products = getLayoutInflater().inflate(R.layout.row_add_product_in_invoice, null, false);

        //Viewing Dynamic Layout
        pname = add_products.findViewById(R.id.pname);
        pqty = add_products.findViewById(R.id.pqty);
        pprice = add_products.findViewById(R.id.pprice);
        removeBtn = add_products.findViewById(R.id.premovebtn);

        //Set subtotal textView
        pprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int price = Integer.parseInt(pprice.getText().toString().trim());
                int qty = Integer.parseInt(pqty.getText().toString().trim());
                double subTotal = (price * qty);
                subtotal.setText(String.valueOf(subTotal));
            }
        });

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

        //Date Formats
        //"dd/MM/yyyy"
        //"MMM dd, yyyy"

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
        if (!validateCustomerName() | !validateInvoiceType() | !validateProduct() |
                !validateProductPrice() | !validateProductQuantity() | !validateShipping()) {
            return;
        }
        Map<String, Object> invoiceMap = new HashMap<>();
        invoiceMap.put("ID", invoiceId.getText().toString().trim());
        invoiceMap.put("invoiceType", invoiceSpinner.getSelectedItem().toString());
        invoiceMap.put("customerName", customer.getText().toString().trim());
        invoiceMap.put("date", dateViewInvoice.getText().toString());
        invoiceMap.put("productName", pname.getText().toString());
        invoiceMap.put("productQuantity", pqty.getText().toString());
        invoiceMap.put("productPrice", pprice.getText().toString());
        invoiceMap.put("subtotal", subtotal.getText().toString());
        invoiceMap.put("shippingCharges", shipping.getText().toString());
        invoiceMap.put("total", total.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Invoice").push()
                .setValue(invoiceMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String _ID = invoiceId.getText().toString().trim();
                        String _Type = invoiceSpinner.getSelectedItem().toString();
                        String _Name = customer.getText().toString().trim();
                        String _Date = dateViewInvoice.getText().toString().trim();
                        String _PName = pname.getText().toString().trim();
                        String _Qty = pqty.getText().toString().trim();
                        String _Price = pprice.getText().toString().trim();
                        String _SubTotal = subtotal.getText().toString().trim();
                        String _Shipping = shipping.getText().toString().trim();
                        String _Total = total.getText().toString().trim();

                        customer.setText("");
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), InvoiceList.class);
                        intent.putExtra("ID", _ID);
                        intent.putExtra("invoiceType", _Type);
                        intent.putExtra("customerName", _Name);
                        intent.putExtra("date", _Date);
                        intent.putExtra("productName", _PName);
                        intent.putExtra("productQuantity", _Qty);
                        intent.putExtra("productPrice", _Price);
                        intent.putExtra("subTotal", _SubTotal);
                        intent.putExtra("shippingCharges", _Shipping);
                        intent.putExtra("total", _Total);
                        startActivity(intent);
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

    private boolean validateInvoiceType() {
        String val = invoiceId.getText().toString().trim();

        if (val.isEmpty()) {
            invoiceId.setError("Field can not be empty");
            return false;
        } else {
            invoiceId.setError(null);
            return true;
        }
    }

    private boolean validateCustomerName() {
        String val = customer.getText().toString().trim();

        if (val.isEmpty()) {
            customer.setError("Field can not be empty");
            return false;
        } else {
            customer.setError(null);
            return true;
        }
    }

    private boolean validateProduct() {
        String val = pname.getText().toString().trim();

        if (val.isEmpty()) {
            pname.setError("Field can not be empty");
            return false;
        } else {
            pname.setError(null);
            return true;
        }
    }

    private boolean validateProductQuantity() {
        String val = pqty.getText().toString().trim();

        if (val.isEmpty()) {
            pqty.setError("Field can not be empty");
            return false;
        } else {
            pqty.setError(null);
            return true;
        }
    }

    private boolean validateProductPrice() {
        String val = pprice.getText().toString().trim();

        if (val.isEmpty()) {
            pprice.setError("Field can not be empty");
            return false;
        } else {
            pprice.setError(null);
            return true;
        }
    }

    private boolean validateShipping() {
        String val = shipping.getText().toString().trim();

        if (val.isEmpty()) {
            shipping.setError("Field can not be empty");
            return false;
        } else {
            shipping.setError(null);
            return true;
        }
    }
}