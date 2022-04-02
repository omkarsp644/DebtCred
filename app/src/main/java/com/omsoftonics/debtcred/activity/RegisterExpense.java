package com.omsoftonics.debtcred.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.adapter.DisplayExpenseRecordsAdapter;
import com.omsoftonics.debtcred.model.Record;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;
import static com.omsoftonics.debtcred.model.MoneyInformation.GetCurrentdate;


public class RegisterExpense extends AppCompatActivity {

    TextView currentDate;
    EditText expenseForWhat,amount;
    TextView addExpense;
    RecyclerView displayExpense;
    DisplayExpenseRecordsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_expense);



        currentDate=(TextView)findViewById(R.id.expense_currentDate);
        currentDate.setText(GetCurrentdate());


        expenseForWhat=(EditText)findViewById(R.id.expense_forwhat);
        amount=(EditText)findViewById(R.id.expense_amountPaid);

        addExpense=(TextView)findViewById(R.id.registerExpense);


        displayExpense=(RecyclerView)findViewById(R.id.displayExpenseRecords);
        adapter=new DisplayExpenseRecordsAdapter(this);
        try {
            displayExpense.setAdapter(adapter);
        }
        catch (Exception e){

        }
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerExpense();
            }
        });
    }

    private void registerExpense() {
        if(amount.getText().toString()==null || amount.getText().toString().equals("") || Integer.parseInt(amount.getText().toString())==0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.amount_greator_than_zero), Toast.LENGTH_SHORT).show();
            amount.setError(getResources().getString(R.string.amount_greator_than_zero));
            return;
        }

        if(expenseForWhat.getText().length()==0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.expense_for_what_req), Toast.LENGTH_SHORT).show();
            expenseForWhat.setError(getString(R.string.expense_for_w));
            return;
        }

        Record e=new Record();
        e.setAmount(Integer.parseInt(amount.getText().toString()));
        e.setDate(currentDate.getText().toString());
        e.setRecordType(MainActivity.RECORD_TYPE_EXPENSE);
        e.setComment(expenseForWhat.getText().toString());
       e.setArrayIndex(currentInformation.getRecord_List().size());


        currentInformation.AddRecord(e);
        e.SaveRecord(this);
        currentInformation.UpdateRecord(e);
        adapter.notifyDataSetChanged();



        amount.getText().clear();
        currentDate.setText(GetCurrentdate());
        expenseForWhat.getText().clear();
        //id.setText(currentEventInformation.GetNextID());


    }


}