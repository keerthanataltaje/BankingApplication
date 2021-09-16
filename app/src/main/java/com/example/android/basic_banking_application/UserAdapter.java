package com.example.android.basic_banking_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Model> mData;
    private LayoutInflater lInflater;
    private ViewAllUsers mClickListener;



    UserAdapter(Context context, List<Model> data) {
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


    // allows clicks events to be caught
    void setClickListener(ViewAllUsers itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}