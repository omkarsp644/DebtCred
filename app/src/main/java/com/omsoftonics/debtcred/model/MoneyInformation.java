package com.omsoftonics.debtcred.model;

import android.content.Context;

import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_EXPENSE;
import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_INCOME;

public class MoneyInformation {
    public int Income_Total;
    public int Expense_Total;

    public ArrayList<Record> record_List =new ArrayList<>();

    public MoneyInformation() {
    }

    public int getIncome_Total() {
        return Income_Total;
    }

    public void setIncome_Total(int income_Total) {
        Income_Total = income_Total;
    }

    public int getExpense_Total() {
        return Expense_Total;
    }

    public void setExpense_Total(int expense_Total) {
        Expense_Total = expense_Total;
    }

    public ArrayList<Record> getRecord_List() {
        return record_List;
    }

    public void setRecord_List(ArrayList<Record> record_List) {
        this.record_List = record_List;
    }

    public MoneyInformation(int income_Total, int expense_Total, ArrayList<Record> record_List) {
        Income_Total = income_Total;
        Expense_Total = expense_Total;
        this.record_List = record_List;
    }

    public void AddRecord(Record data) {
        if(this.record_List ==null){
            this.record_List =new ArrayList<>();
        }
        data.setArrayIndex(this.record_List.size());
        this.record_List.add(data);

        if (data.getRecordType()==RECORD_TYPE_INCOME){
            this.Income_Total+=data.getAmount();
        }
        else if (data.getRecordType()==RECORD_TYPE_EXPENSE){
            this.Expense_Total+=data.getAmount();
        }
        else{
            return;
        }
    }

    public void UpdateRecord(Record data) {
        Record rec= (Record) this.record_List.stream().filter(p->p.getArrayIndex()==data.getArrayIndex()).findFirst().orElse(null);
        this.record_List.set(rec.getArrayIndex(),data);


        if (data.getRecordType()==RECORD_TYPE_INCOME){
            this.Income_Total+=data.getAmount();
            this.Income_Total-=rec.getAmount();
        }
        else if (data.getRecordType()==RECORD_TYPE_EXPENSE){
            this.Expense_Total+=data.getAmount();
            this.Expense_Total-=rec.getAmount();
        }
        else{
            return;
        }

    }


    public void DeleteRecord(Record data){
        Record rec= (Record) this.record_List.stream().filter(p->p.getArrayIndex()==data.getArrayIndex()).findFirst().orElse(null);

        if (data.getRecordType()==RECORD_TYPE_INCOME){
            this.Income_Total-=rec.getAmount();
        }
        else if (data.getRecordType()==RECORD_TYPE_EXPENSE){
            this.Expense_Total-=rec.getAmount();
        }
        else{
            return;
        }

    }




    public static String GetCurrentdate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public void ResetAllData(Context context){
        SqliteDatabaseHelper databaseHelper=new SqliteDatabaseHelper(context);
        databaseHelper.ResetAll();
    }



}
