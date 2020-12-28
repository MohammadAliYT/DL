package com.example.dl.Contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dl.Databases.ContactHelperClass;
import com.example.dl.HelperClasses.ContactAdapter;
import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ContactList extends AppCompatActivity {

    //Variables
    RecyclerView recview;
    FloatingActionButton fabAddContacts;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fabAddContacts = findViewById(R.id.addContact);
        recview=(RecyclerView)findViewById(R.id.contact_recycle_view);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ContactHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ContactHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Contacts"), ContactHelperClass.class)
                        .build();

        adapter= new ContactAdapter(options);
        recview.setAdapter(adapter);

        fabAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactList.this, addContacts.class);
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