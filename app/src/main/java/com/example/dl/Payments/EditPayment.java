package com.example.dl.Payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dl.HelperClasses.DatePickerFragment;
import com.example.dl.Invoice.InvoiceList;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditPayment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Variables
    private SignaturePad editSignaturePad;
    private Button editClearButton;
    private Button editSaveButton;
    //Layout Buttons
    Button editcalendarBtn;
    TextView editdate, editamount;
    TextInputLayout editinvoiceId;

    //Intent Data
    String id, amount, date;

    //Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //FB Database Initializing
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment);

        verifyStoragePermissions(this);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        editcalendarBtn = findViewById(R.id.edit_calendarButton);
        editdate = findViewById(R.id.edit_dateTextViewPayments);
        editamount = findViewById(R.id.edit_amountP);
        editinvoiceId = findViewById(R.id.edit_payments_customer_name);
        editSignaturePad = findViewById(R.id.edit_signature_pad);

        //Passing Data Via Intent
        Intent intent = getIntent();
        id = intent.getStringExtra("invoiceID");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");

        editinvoiceId.getEditText().setText(id);
        editamount.setText(amount);
        editdate.setText(date);

        //Action For Calendar Button
        editcalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        //Date
        String date_edit = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        editdate.setText(date_edit);

        editSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(EditPayment.this, "Add Signature", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                editSaveButton.setEnabled(true);
                editClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                editSaveButton.setEnabled(false);
                editClearButton.setEnabled(false);
            }
        });

        editClearButton = findViewById(R.id.clearSignature);
        editSaveButton = findViewById(R.id.updatePayment);

        editClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSignaturePad.clear();
            }
        });

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = editSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(EditPayment.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                    updateData();
                } else {
                    Toast.makeText(EditPayment.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                if (addSvgSignatureToGallery(editSignaturePad.getSignatureSvg())) {
                    Toast.makeText(EditPayment.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditPayment.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateData(){
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("invoiceID",editinvoiceId.getEditText().getText().toString());
        updateData.put("amount",editamount.getText().toString());
        updateData.put("date",editdate.getText().toString());

        //Initialize FB Db
        reference = FirebaseDatabase.getInstance().getReference().child("Payments");
        String key = getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference().child("Payments")
                .child(key).updateChildren(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPayment.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PaymentList.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPayment.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditPayment.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("PDF", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePadEdits"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        EditPayment.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            @SuppressLint("DefaultLocale") File svgFile = new File(getAlbumStorageDir("SignaturePadEdit"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dateText = findViewById(R.id.edit_dateTextViewPayments);
        dateText.setText(currentDateString);

    }

    public void goToPaymentList(View view) {
        startActivity(new Intent(getApplicationContext(), PaymentList.class));
        finish();
    }

}