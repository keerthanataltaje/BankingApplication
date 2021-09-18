package com.example.android.basic_banking_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
class SendToAdapter extends RecyclerView.Adapter<SendToAdapter.ViewHolder> {

    private List<Model> mData;
    private LayoutInflater lInflater;
    private SendTo mClickListener;



    SendToAdapter(Context context, List<Model> data) {
        this.lInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = lInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model details =  mData.get(position);
        holder.User_name.setText(details.getName());
        holder.User_accountnumber.setText(details.getAccount());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView User_name;
        TextView User_accountnumber;

        ViewHolder(View itemView) {
            super(itemView);
            User_name = itemView.findViewById(R.id.UserName);
            User_accountnumber=itemView.findViewById(R.id.User_accountNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        Model data=mData.get(id);
        return data.getContact_number();
    }
    String getname(int id) {
        Model data=mData.get(id);
        return data.getName();
    }

    // allows clicks events to be caught
    void setClickListener(SendTo itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

public class SendTo extends AppCompatActivity {
    DatabaseHandler dbhandler;
    List<Model> senduserlist = new ArrayList<>();
    SendToAdapter adapter;
    String contact_no,transferableamount,date,username,to_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);
        Intent intent = getIntent();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
         date = dateFormat.format(cal.getTime());

        contact_no = intent.getStringExtra("ID");
        transferableamount=intent.getStringExtra("transferamount");
        username=intent.getStringExtra("name");
        show_send_Users();

    }
    public void show_send_Users()
    {
        Cursor cursor = new DatabaseHandler(this).readUserData();
        while(cursor.moveToNext())
        {
            String account_number= cursor.getString(3);
            String name = cursor.getString(1);
            String contact_number = cursor.getString(0);
            Model model = new Model(contact_number,name,account_number);
            senduserlist.add(model);
        }

        RecyclerView recyclerView = findViewById(R.id.sendusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SendToAdapter(this, senduserlist);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    public void onItemClick(View view, int position) {
        String fromuser=adapter.getname(position);
        String fromnum=adapter.getItem(position);
        if(adapter.getItem(position).equals(contact_no))
            Toast.makeText(this, "You are transfering money to yourself,INVALID!!!", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "please confirm", Toast.LENGTH_SHORT).show();
            to_name=adapter.getname(position);
            getUserConfirmation(adapter.getItem(position), adapter.getname(position),fromuser,fromnum);
        }
    }
    private void getUserConfirmation(String num,String name,String fromname,String fromnum){
        final AlertDialog.Builder cbuilder = new AlertDialog.Builder(SendTo.this);
        View confirm= getLayoutInflater().inflate(R.layout.activity_confirm, null);
        cbuilder.setView(confirm)
                .setTitle("Please Confirm");
        cbuilder.setMessage("You are transferring Rs "+transferableamount+ " to "+name+"\n Contact Number: "+num );
        cbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do something
                Cursor cursor = new DatabaseHandler(SendTo.this).readParticularUser(num);
                while (cursor.moveToNext()) {

                    String user_balance = cursor.getString(4);
                    Double users_balance = Double.parseDouble(user_balance);
                    Double users_transferamount = Double.parseDouble(transferableamount);
                    Double users_updatedamount = users_balance + users_transferamount;

                    new DatabaseHandler(SendTo.this).insertTransferDetails(date, username, name, transferableamount, "Success");
                    new DatabaseHandler(SendTo.this).updateAmount(num, users_updatedamount.toString());
                    DeductAmount(users_balance,transferableamount,contact_no);
                    Toast.makeText(SendTo.this, "Transaction Successful!", Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(SendTo.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
                cbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DatabaseHandler(SendTo.this).insertTransferDetails(date, username, name, transferableamount, "Failed");
                        Toast.makeText(SendTo.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
                        transferableamount=null;
                        dialog.dismiss();
                        Intent intent = new Intent(SendTo.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = cbuilder.create();
        dialog.show();
    }
    public void onBackPressed() {
        AlertDialog.Builder exit_option = new AlertDialog.Builder(SendTo.this);
        exit_option.setTitle("Transaction Cancelled,\n Are you sure? ").setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DatabaseHandler(SendTo.this).insertTransferDetails(date, username, to_name, transferableamount, "Failed");
                        Toast.makeText(SendTo.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SendTo.this, MainActivity.class));
                        finish();
                    }
                }).setNegativeButton("NO", null);
        AlertDialog alertexit = exit_option.create();
        alertexit.show();
        transferableamount=null;

        Intent intent = new Intent(SendTo.this,MainActivity.class);
        startActivity(intent);
    }
    private void DeductAmount(Double bal,String transferable,String fromnumber) {
        Double current = bal;
        Double transfer = Double.parseDouble(transferable);
        Double difference = current-transfer;

        new DatabaseHandler(this).updateAmount(fromnumber, difference.toString());
    }

}