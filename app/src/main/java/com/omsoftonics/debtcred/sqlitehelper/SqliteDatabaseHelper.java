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

    private static final String DATABASE_NAME="DebtCredMoneyManager";
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

                informations.add(info);

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

    public void ExportData(Activity context) {
        Toast.makeText(context,"Import and Export Functionality will be available soon...", Toast.LENGTH_SHORT).show();

        //+"VarganiBackup_" + GetCurrentdate().replace('/', '_') + ".txt"

//        boolean success = true;
//        if (!BACKUP_IMPORT_EXPORT.exists())
//            success = BACKUP_IMPORT_EXPORT.mkdirs();
//        if (success) {
//            try {
//                ArrayList<Record> vargani=GetAllVarganiReciepts();
//                ArrayList<Expanditures> expanditures=GETAllExpanditures();
//                ArrayList<EventInformation> events=GetAllEvents();
//
//
//
//                FileWriter fileWriter=new FileWriter(BACKUP_IMPORT_EXPORT);
//
//
//                fileWriter.write("TABLE 1 STARTED\n");
//                for (EventInformation e:events){
//                    fileWriter.append(""+e);
//                    fileWriter.append("\n");
//                }
//
//
//
//                fileWriter.write("\nTABLE 2 STARTED\n");
//                for (Record e:vargani){
//                    fileWriter.append(""+e);
//                    fileWriter.append("\n");
//                }
//
//
//
//                fileWriter.write("\nTABLE 3 STARTED\n");
//                for (Expanditures e:expanditures){
//                    fileWriter.append(""+e);
//                    fileWriter.append("\n");
//                }
//
//
//
//                fileWriter.flush();
//                fileWriter.close();
//
//                Toast.makeText(context,"Exported...",Toast.LENGTH_SHORT).show();
//
//
//            }
//            catch (Exception e){
//                Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }


    }


    public void ImportData(Activity context) {

        Toast.makeText(context,"Import and Export Functionality will be available soon...", Toast.LENGTH_SHORT).show();

//
//        File csvFile=BACKUP_IMPORT_EXPORT;
//
//
//        if(!csvFile.exists()){
//            Toast.makeText(context,"File Doesn't Exist...",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int current=0;
//        try{
//            CSVReader csvReader=new CSVReader(new FileReader(csvFile.getAbsolutePath()));
//            String[] nextLine;
//
//
//            while ((nextLine=csvReader.readNext())!=null){
//                if(!nextLine.equals("")) {
//                    if (nextLine.equals("TABLE 1 STARTED")) {
//                        current = 0;
//                    } else if (nextLine.equals("TABLE 2 STARTED")) {
//                        current = 1;
//                    } else if (nextLine.equals("TABLE 3 STARTED")) {
//                        current = 2;
//                    }
//
//
//
//
//                    if(current==0){
//
//                        EventInformation event=new EventInformation();
//                        event.setDateCreated(nextLine[0]);
//                        event.seteventName(nextLine[1]);
//                        event.setEventIncome(Integer.parseInt(nextLine[2]));
//                        event.setEventExp(Integer.parseInt(nextLine[3]));
//                        event.setEventPending(Integer.parseInt(nextLine[4]));
//                        event.SaveRecord(context);
//
//                    }
//                    else if(current==1){
//
//                    }
//                    else if(current==2){
//
//                    }
//
//                }
//            }
//        }
//        catch (Exception e){
//
//        }
//
//

    }
}


