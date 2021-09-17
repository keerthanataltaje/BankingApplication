package com.example.android.basic_banking_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button viewAllUsers,view_Transfers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewAllUsers = findViewById(R.id.viewusers);
        view_Transfers=findViewById(R.id.viewtransfers);
        viewAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ViewAllUsers.class);
                startActivity(i);
            }
        });
        view_Transfers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(MainActivity.this,ViewTransferData.class);
                startActivity(i2);
            }
        });
    }
}