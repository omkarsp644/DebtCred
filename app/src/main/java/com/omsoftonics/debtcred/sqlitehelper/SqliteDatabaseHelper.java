package com.omsoftonics.debtcred.sqlitehelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import com.omsoftonics.debtcred.model.Record;
import com.omsoftonics.debtcred.model.MoneyInformation;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_EXPENSE;
import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_INCOME;

public class SqliteDatabaseHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="DebtCredMoneyManager";
    private static final String MONEY_INFO="MONEY_INFORMATION";


    private static final String ID="ID";
    private static final String COMMENT="COMMENT";
    private static final String DATE="DATE";
    private static final String AMOUNT="AMOUNT";
    private static final String RECORDTYPE="RECORDTYPE";





    private static final String QUERY_TABLE_MONEY_INFO="CREATE TABLE "+MONEY_INFO+" (\n" +
            ID+"   INTEGER PRIMARY KEY AUTOINCREMENT   ,\n" +
            COMMENT+"   VARCHAR(100) ," +
            DATE+"   VARCHAR(100) ," +
            AMOUNT+"   INT," +
            RECORDTYPE+"   INT" +
            ")";

    private static final String DROP_IF_EXIST="DROP TABLE IF EXISTS ";

    SQLiteDatabase database;

    public SqliteDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public SqliteDatabaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    public SqliteDatabaseHelper (Context context){
        super(context,DATABASE_NAME,null,1);
        database=this.getWritableDatabase();
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(QUERY_TABLE_MONEY_INFO);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_IF_EXIST+MONEY_INFO);

        onCreate(db);
    }



    @SuppressLint("Range")
    public MoneyInformation SetupAllInformation(){
        database=this.getReadableDatabase();

        String query="SELECT rowid as RowID,* FROM "+MONEY_INFO+" ORDER BY "+DATE+" DESC";
        ArrayList<Record> record =new ArrayList<>();

        MoneyInformation infor=new MoneyInformation();

        Cursor cursor=database.rawQuery(query,null);
        // cursor.moveToFirst();

        if(cursor.moveToFirst()) {
            do {
                Record info = new Record();

                info.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                info.setDatabaseID(Integer.parseInt(String.valueOf(cursor.getColumnIndex(ID))));
                info.setAmount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(AMOUNT))));
                info.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                int d=Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECORDTYPE)));
                info.setRecordType(d==RECORD_TYPE_INCOME?RECORD_TYPE_INCOME:RECORD_TYPE_EXPENSE);

                infor.AddRecord(info);

            } while (cursor.moveToNext());
        }
        return infor;

    }

    @SuppressLint("Range")
    public ArrayList<Record> GetSpecificIncomeRecords(String s) {
        database=this.getReadableDatabase();

        String query="SELECT * FROM "+MONEY_INFO+" WHERE "+COMMENT+" LIKE "+" '%"+s+"%' OR "+DATE+" LIKE "+" '%"+s+"%' OR "+ID+" LIKE "+" '%"+s+"%'";
        ArrayList<Record> informations=new ArrayList<>();


        Cursor cursor=database.rawQuery(query,null);
        // cursor.moveToFirst();
        if(cursor.moveToFirst()) {
            do {
                Record info = new Record();

                info.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                info.setDatabaseID(Integer.parseInt(String.valueOf(cursor.getColumnIndex(ID))));
                info.setAmount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(AMOUNT))));
                info.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                int d=Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECORDTYPE)));
                info.setRecordType(d==RECORD_TYPE_INCOME?RECORD_TYPE_INCOME:RECORD_TYPE_EXPENSE);

                informations.add(0,info);

            } while (cursor.moveToNext());
        }

        return informations;
    }


    public long InsertData(Record info) {

        database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(DATE,info.getDate());
        values.put(AMOUNT,info.getAmount());
        values.put(COMMENT,info.getComment());
        values.put(RECORDTYPE,info.getRecordType());
        values.put(ID,(getLastRecordID()+1));

        return database.insert(MONEY_INFO,null,values);

    }

    public long UpdateRecord(Record info) {
        database=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(DATE,info.getDate());
        values.put(AMOUNT,info.getAmount());
        values.put(COMMENT,info.getComment());
        values.put(ID,info.getDatabaseID());
        values.put(RECORDTYPE,info.getRecordType());
        return database.update(MONEY_INFO,values,ID+"= ?", new String[]{Integer.toString(info.getDatabaseID())});
    }

    public long RemoveExpanditure(Record info) {
        database=this.getWritableDatabase();
        return database.delete(MONEY_INFO,ID+"= ?", new String[]{Integer.toString(info.getDatabaseID())});
    }

    public void ResetAll(){
        database=this.getWritableDatabase();
        database.execSQL("drop table "+MONEY_INFO);
        database.execSQL(QUERY_TABLE_MONEY_INFO);
    }

    @SuppressLint("Range")
    public int getLastRecordID(){
        String s="select count(*) as Count from "+MONEY_INFO;

        Cursor cursor=database.rawQuery(s,null);
        // cursor.moveToFirst();

        if(cursor.moveToFirst()) {
            do {
                Record info = new Record();

               return Integer.parseInt(cursor.getString(cursor.getColumnIndex("Count")));
            } while (cursor.moveToNext());
        }
        return 0;
    }

}


