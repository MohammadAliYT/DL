package com.example.dl.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dl.Common.LoginSignup.StartUpScreen;
import com.example.dl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class addContacts extends AppCompatActivity {

    //Variables
    Button save;
    TextInputLayout name, address, phone, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        save = findViewById(R.id.saveContact);
        name = findViewById(R.id.contact_name);
        address = findViewById(R.id.contact_address);
        phone = findViewById(R.id.contact_phone);
        balance = findViewById(R.id.contact_balance);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });
    }

    private void processinsert()
    {
        Map<String,Object> map=new HashMap<>();
        map.put("name",name.getEditText().getText().toString());
        map.put("address",address.getEditText().getText().toString());
        map.put("phone",phone.getEditText().getText().toString());
        map.put("balance",balance.getEditText().getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Contacts").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.getEditText().setText("");
                        address.getEditText().setText("");
                        phone.getEditText().setText("");
                        balance.getEditText().setText("");
                        Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ContactList.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Could not insert",Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void goToContactList(View view) {
        startActivity(new Intent(getApplicationContext(), ContactList.class));
        finish();
    }
}