    package com.omsoftonics.debtcred.model;

import android.content.Context;
import android.widget.Toast;

import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

    public class Record {
        public String date;
        public int amount;
        public String comment;
        public int ArrayIndex;
        public int DatabaseID;
        public int recordType=0;

        public int getRecordType() {
            return recordType;
        }

        public void setRecordType(int recordType) {
            this.recordType = recordType;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getArrayIndex() {
            return ArrayIndex;
        }

        public void setArrayIndex(int arrayIndex) {
            ArrayIndex = arrayIndex;
        }

        public int getDatabaseID() {
            return DatabaseID;
        }

        public void setDatabaseID(int databaseID) {
            DatabaseID = databaseID;
        }

        public Record() {
        }

        public Record(String date, int amount, String comment, int arrayIndex, int databaseID) {
            this.date = date;
            this.amount = amount;
            this.comment = comment;
            ArrayIndex = arrayIndex;
            DatabaseID = databaseID;
        }

        public boolean SaveRecord(Context context){

            SqliteDatabaseHelper databaseHelper=new SqliteDatabaseHelper(context);
            long result=databaseHelper.InsertData(this);
            if (result!=-1){
                Toast.makeText(context,"Record Successfully Registered....", Toast.LENGTH_SHORT).show();
                this.setDatabaseID((int)result);
                return true;
            }
            else{
                Toast.makeText(context,"Record Not Registered ....", Toast.LENGTH_SHORT).show();

                return false;

            }
        }


        public boolean UpdateRecord(Context context){

            SqliteDatabaseHelper databaseHelper=new SqliteDatabaseHelper(context);
            long result=databaseHelper.UpdateRecord(this);
            if (result!=-1){
                //Toast.makeText(context,"Event Information Updated Successfully....",Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                //Toast.makeText(context,"Event Not Updated ....",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        public boolean RemoveRecord(Context context){

            SqliteDatabaseHelper databaseHelper=new SqliteDatabaseHelper(context);
            long result=databaseHelper.RemoveExpanditure(this);
            if (result!=-1){
                Toast.makeText(context,"Event Information Removed Successfully....", Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                Toast.makeText(context,"Event Not Removed ....", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


    }
