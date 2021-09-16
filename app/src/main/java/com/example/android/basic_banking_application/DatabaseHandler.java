package com.example.android.basic_banking_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
   private String Table = "user";
   private String tTable="transfertable";

    public DatabaseHandler(@Nullable Context context) {
        super(context,"Users.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table "+Table+" (USER_ID INTEGER PRIMARY KEY, NAME TEXT,EMAIL VARCHAR,ACCOUNTNUMBER VARCHAR,BALANCE DECIMAL)");
      db.execSQL("create table "+tTable+"(TRANSACTIONID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,NAMEFROM TEXT,NAMETO TEXT,AMOUNT DECIMAL,STATUS TEXT)");
                    db.execSQL("insert into user values(9123477780,'John','johndoe@example.com','XXXXXXXXXX08',150000.00)");
                    db.execSQL("insert into user values(9375682345,'Kaia','kaia@example.com','XXXXXXXXXX56',20000.00)");
                    db.execSQL("insert into user values(9624318928,'Rohan','rohan@example.com','XXXXXXXXXX11',35000.00)");
                    db.execSQL("insert into user values(9945134680,'Priti','priti@example.com','XXXXXXXXXX89',15000.00)");
                    db.execSQL("insert into user values(9799492933,'Tara','tara@example.com','XXXXXXXXXX65',1350000.00)");
                    db.execSQL("insert into user values(9999445578,'Varun','varun@example.com','XXXXXXXXXX32',250000.00)");
                    db.execSQL("insert into user values(9845129068,'Priya','priya@example.com','XXXXXXXXXX19',160000.00)");
                    db.execSQL("insert into user values(9135558045,'George','george@example.com','XXXXXXXXXX05',26000.00)");
                    db.execSQL("insert into user values(9512789062,'Amulya','amulya@example.com','XXXXXXXXXX03',90000.00)");
                    db.execSQL("insert into user values(9237855678,'Krishna','krishna@example.com','XXXXXXXXXX33',65000.00)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int older, int newer) {
        db.execSQL("DROP TABLE IF EXISTS "+Table);
        db.execSQL("DROP TABLE IF EXISTS "+tTable);
        onCreate(db);
    }
    public Cursor readUserData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user",null);
        return cursor;
    }
    public Cursor readParticularUser(String contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where USER_ID="+contact,null);
        return cursor;
    }
    public void updateAmount(String phonenumber, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update user set BALANCE = " + amount + " where USER_ID = " +phonenumber);
    }
    public boolean insertTransferDetails(String date,String from_, String to_, String amount, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", date);
        contentValues.put("NAMEFROM", from_);
        contentValues.put("NAMETO", to_);
        contentValues.put("AMOUNT", amount);
        contentValues.put("STATUS", status);
        long stat = db.insert(tTable, null, contentValues);
        if(stat == -1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor readtransfers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from transfertable", null);
        return cursor;
    }
}
