package com.example.android.basic_banking_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUserDetails extends AppCompatActivity {
    TextView username,contactnumber,balance_amount,accnum,user_email;
    Button transfer_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_details);
         username= (TextView)findViewById(R.id.users_name);
        contactnumber= (TextView)findViewById(R.id.users_contact);
         balance_amount=(TextView) findViewById(R.id.users_bal);
         accnum= (TextView)findViewById(R.id.users_accno);
        user_email= (TextView)findViewById(R.id.users_emailid);
        transfer_amt=(Button) findViewById(R.id.transfer_money);

        Intent intent = getIntent();

        String contact_no = intent.getStringExtra("Id");

        try {

            Cursor cursor = new DatabaseHandler(this).readParticularUser(contact_no);

            while(cursor.moveToNext()) {
                String account_number = cursor.getString(cursor.getColumnIndex("ACCOUNTNUMBER"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));

                String contact_number = cursor.getString(cursor.getColumnIndex("USER_ID"));
                String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                String balance = cursor.getString(cursor.getColumnIndex("BALANCE"));
                contactnumber.setText(contact_number);
                username.setText(name);
                accnum.setText(account_number);
                user_email.setText(email);
                balance_amount.setText(balance);
                Toast.makeText(this, "hello " + name, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("banking-app-error", String.valueOf(e));
        }

       transfer_amt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               enteramount();
           }
       });

    }
    private void enteramount()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserDetails.this);
        View amountview = getLayoutInflater().inflate(R.layout.activity_enter_amount, null);
        builder.setView(amountview)
                .setTitle("Enter the Amount");
        final EditText mAmount = (EditText) amountview.findViewById(R.id.amount);
        builder.setPositiveButton("TRANSFER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mAmount.getText().toString().isEmpty())
                {
                    mAmount.setError("Amount cannot be empty");
                    Toast.makeText(ViewUserDetails.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(mAmount.getText().toString()) > Double.parseDouble(String.valueOf(balance_amount.getText().toString()))){
                    mAmount.setError("Your account dosen't have enough balance");
                    Toast.makeText(ViewUserDetails.this, "Your account dosen't have enough balance", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(ViewUserDetails.this, SendTo.class);
                    intent.putExtra("ID", contactnumber.getText().toString());
                    intent.putExtra("name", username.getText().toString());
                    intent.putExtra("currentamount",balance_amount.toString());
                    intent.putExtra("transferamount", mAmount.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                cancelTransaction();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    private void cancelTransaction()
    {
        Toast.makeText(ViewUserDetails.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
    }
}