package com.example.android.basic_banking_application;

public class Model {
    String contact_number, name, acc_no,email;
    double balance;
    public Model(String phno, String name, String acc_no){
        this.contact_number=phno;
        this.name=name;
        this.acc_no=acc_no;
    }
    public Model(String phno, String name, String acc_no,String email, double balance)
    {
        this.contact_number=phno;
        this.name=name;
        this.acc_no=acc_no;
        this.email=email;
        this.balance=balance;
    }
    public String getContact_number()
    {
        return contact_number;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return acc_no;
    }
    public String getEmail()
    {
        return email;
    }
    public double getBalance()
    {
        return balance;
    }

}
