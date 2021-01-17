package com.example.dl.Invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dl.Databases.InvoiceHelperClass;
import com.example.dl.Payments.addPayment;
import com.example.dl.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewInvoice extends AppCompatActivity {

    TextView name, id, total, date, pname, pprice, pqty, shipping, subtotal, type;
    ImageView delete, pdf;
    DatabaseReference viewInvoiceRef, deleteRef, pdfRef;
    Bitmap bitmap, scaledBitmap;
    int pageWidth = 1200;

    //Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);

        verifyStoragePermissions(this);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        name = findViewById(R.id.invoiceNameText);
        id = findViewById(R.id.invoiceIdText);
        total = findViewById(R.id.invoiceAmountText);
        pname = findViewById(R.id.invoiceProductNameText);
        pprice = findViewById(R.id.invoicePriceText);
        pqty = findViewById(R.id.invoiceQuantityText);
        shipping = findViewById(R.id.invoiceShippingText);
        subtotal = findViewById(R.id.invoiceSubtotalText);
        date = findViewById(R.id.invoiceDateTextView);

        //Button Hooks
        delete = findViewById(R.id.invoiceDeleteBtn);
        pdf = findViewById(R.id.invoicePDFBtn);

        //Image to View in PDF Invoice
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dllogo);
        //Scaling Image
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

        //Initialize FB Db
        viewInvoiceRef = FirebaseDatabase.getInstance().getReference().child("Invoice");
        String key = getIntent().getStringExtra("key");
        viewInvoiceRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameT = snapshot.child("customerName").getValue().toString();
                    name.setText(nameT);
                    String idT = snapshot.child("ID").getValue().toString();
                    id.setText("ID: " + idT);
                    String totalT = snapshot.child("total").getValue().toString();
                    total.setText("Total: " + totalT + " Rs.");
                    String pnameT = snapshot.child("productName").getValue().toString();
                    pname.setText(pnameT);
                    String ppriceT = snapshot.child("productPrice").getValue().toString();
                    pprice.setText(ppriceT + " Rs.");
                    String pqtyT = snapshot.child("productQuantity").getValue().toString();
                    pqty.setText(pqtyT + " Pcs");
                    String shippingT = snapshot.child("shippingCharges").getValue().toString();
                    shipping.setText("Shipping Charges \n" + shippingT + " Rs.");
                    String subtotalT = snapshot.child("subtotal").getValue().toString();
                    subtotal.setText("\n" + subtotalT + " Rs.");
                    String dateT = snapshot.child("date").getValue().toString();
                    date.setText(dateT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Delete Function
        deleteRef = FirebaseDatabase.getInstance().getReference().child("Invoice").child(key);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewInvoice.this);
                builder.setTitle("Delete Invoice");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Invoice")
                                .child(deleteRef.getKey()).removeValue();
                        startActivity(new Intent(getApplicationContext(), InvoiceList.class));
                        finish();
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

        //PDF Function
        pdfRef = FirebaseDatabase.getInstance().getReference().child("Invoice").child(key);
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePDF();
                Toast.makeText(ViewInvoice.this, "Invoice has been converted to PDF Form, Check Internal Storage to view Invoice", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void goToInvoiceList(View view) {
        startActivity(new Intent(ViewInvoice.this, InvoiceList.class));
        finish();
    }

    private void makePDF() {

        PdfDocument invoicePDF = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo invoicePageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page invoicePage = invoicePDF.startPage(invoicePageInfo);

        Canvas canvas = invoicePage.getCanvas();
        canvas.drawBitmap(scaledBitmap, 500, 30, paint);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(100);
        canvas.drawText("Invoice", pageWidth / 2, 330, title);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(40f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
        paint.setColor(Color.BLACK);
        canvas.drawText("Customer Name: " + name.getText(), 20, 590, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(25f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Invoice Type: Credit Bill", pageWidth - 20, 590, paint);
        canvas.drawText("Invoice : " + id.getText(), pageWidth - 20, 640, paint);
        canvas.drawText("Date: " + date.getText(), pageWidth - 20, 690, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 780, pageWidth - 20, 860, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Sr No.", 40, 830, paint);
        canvas.drawText("Item Description", 200, 830, paint);
        canvas.drawText("Price", 700, 830, paint);
        canvas.drawText("Qty", 900, 830, paint);
        canvas.drawText("Total", 1050, 830, paint);

        canvas.drawLine(180, 790, 180, 840, paint);
        canvas.drawLine(680, 790, 680, 840, paint);
        canvas.drawLine(880, 790, 880, 840, paint);
        canvas.drawLine(1030, 790, 1030, 840, paint);

//        int qty = Integer.parseInt();
//        int price = Integer.parseInt();

        canvas.drawText("1. ", 40, 950, paint);
        canvas.drawText(String.valueOf(pname.getText()), 200, 950, paint);
        canvas.drawText(String.valueOf(pprice.getText()), 700, 950, paint);
        canvas.drawText(String.valueOf(pqty.getText()), 900, 950, paint);
        canvas.drawText(String.valueOf(subtotal.getText()), 1000, 950, paint);

        canvas.drawLine(680, 1200, pageWidth - 20, 1200, paint);
        canvas.drawText("Sub Total:" + subtotal.getText(), 700, 1250, paint);
        canvas.drawText("" + shipping.getText(), 700, 1300, paint);

        paint.setColor(Color.rgb(102, 255, 102));
        canvas.drawRect(680, 1350, pageWidth - 20, 1450, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("" + total.getText(), 700, 1417, paint);

        invoicePDF.finishPage(invoicePage);
        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/Doc.pdf";
        File photo = new File(getAlbumStorageDir("DigiLedger PDF"), String.format("Invoice_%d.pdf", System.currentTimeMillis()));
        //File myFile = new File(myFilePath);
        try {
            invoicePDF.writeTo(new FileOutputStream(photo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        invoicePDF.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ViewInvoice.this, "Cannot write PDF to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), albumName);
        if (!file.mkdirs()) {
            Log.e("DigiLedger PDF", "Directory not created");
        }
        if (file.mkdirs()) {
            Log.e("DigiLedger PDF", "Directory created");
        }
        return file;
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
}