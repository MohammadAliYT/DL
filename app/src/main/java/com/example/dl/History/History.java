package com.example.dl.History;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.dl.R;
import com.example.dl.User.UserDashboard;
import com.google.firebase.firestore.auth.User;

//17.1.2
public class History extends AppCompatActivity {

    Button customerH,productH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        customerH = findViewById(R.id.customerHistory);
        productH = findViewById(R.id.productHistory);

        customerH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, CustomerHistory.class);
                startActivity(intent);
            }
        });

        productH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, ProductHistory.class);
                startActivity(intent);
            }
        });
    }

    public void goToHomeFromHistory(View view) {
        Intent intent = new Intent(History.this, UserDashboard.class);
        startActivity(intent);
    }
}