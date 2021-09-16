package com.example.android.basic_banking_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
 class TransferModel {
    String from_, to_, amt,time_;
    double balance;
    public TransferModel(String from_user, String to_user, String amount,String timestamp)
    {
        this.from_=from_user;
        this.to_=to_user;
        this.amt=amount;
        this.time_=timestamp;
    }
    public String getFrom_()
    {
        return from_;
    }

    public String getTo_() {
        return to_;
    }

    public String getAmt() {
        return amt;
    }

    public String getTime_() {
        return time_;
    }
}
class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {

    private List<TransferModel> mData;
    private LayoutInflater lInflater;
    private ViewTransferData mClickListener;



    TransferAdapter(Context context, List<TransferModel> data) {
        this.lInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = lInflater.inflate(R.layout.recyclerview_for_transactions, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransferModel details =  mData.get(position);
        holder.from_user.setText(details.getFrom_());
        holder.to_user.setText(details.getTo_());
        holder.amount_.setText(details.getAmt());
        holder.time_.setText(details.getTime_());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView from_user,to_user,amount_,time_;


        ViewHolder(View itemView) {
            super(itemView);
            from_user = itemView.findViewById(R.id.fromuser);
            to_user=itemView.findViewById(R.id.touser);
            amount_=itemView.findViewById(R.id.t_amount);
            time_=itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        TransferModel data=mData.get(id);
        return data.getFrom_();
    }


    // allows clicks events to be caught
    void setClickListener(ViewTransferData itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

public class ViewTransferData extends AppCompatActivity {

    List<TransferModel> transferlist = new ArrayList<>();
    TransferAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transfer_data);
        showTransfers();
    }

    public void showTransfers() {
        Cursor cursor = new DatabaseHandler(this).readtransfers();
        while (cursor.moveToNext()) {
            String from = cursor.getString(2);
            String to = cursor.getString(3);
            String amount = cursor.getString(4);
            String time = cursor.getString(1);
            TransferModel model = new TransferModel(from, to, amount, time);
            transferlist.add(model);
        }

        RecyclerView recyclerView = findViewById(R.id.rvtransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransferAdapter(this, transferlist);
        adapter.setClickListener(ViewTransferData.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewTransferData.this, ViewUserDetails.class);
        intent.putExtra("Id", adapter.getItem(position));
        startActivity(intent);

    }
}