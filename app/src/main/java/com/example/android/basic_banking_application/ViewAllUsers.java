package com.example.android.basic_banking_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewAllUsers extends AppCompatActivity {
    DatabaseHandler dbhandler;
    List<Model> userlist = new ArrayList<>();
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_users);

       /* dbhandler= new DatabaseHandler(ViewAllUsers.this);
        Cursor cursor = dbhandler.readUserData();*/
        showUsers();
    }
    public void showUsers()
    {
        Cursor cursor = new DatabaseHandler(this).readUserData();
        while(cursor.moveToNext())
        {
            String account_number= cursor.getString(3);
            String name = cursor.getString(1);
            String contact_number = cursor.getString(0);
            Model model = new Model(contact_number,name,account_number);
            userlist.add(model);
        }

        RecyclerView recyclerView = findViewById(R.id.rvusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, userlist);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewAllUsers.this,ViewUserDetails.class);
        intent.putExtra("Id",adapter.getItem(position));
        startActivity(intent);


    }
}