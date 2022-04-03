package com.omsoftonics.debtcred.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.adapter.DisplayExpenseRecordsAdapter;
import com.omsoftonics.debtcred.model.Record;
import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;

public class DisplayAllTransactions extends AppCompatActivity {

    RecyclerView displayExpense;
    DisplayExpenseRecordsAdapter adapter;
    SqliteDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_transactions);

        helper=new SqliteDatabaseHelper(this);


        displayExpense=(RecyclerView)findViewById(R.id.displayExpenseRecords);
        adapter=new DisplayExpenseRecordsAdapter(DisplayAllTransactions.this, currentInformation.getRecord_List());
        try {
            displayExpense.setAdapter(adapter);
        }
        catch (Exception e){

        }


        SearchView searchView=(SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displayExpense.removeAllViews();


                adapter=new DisplayExpenseRecordsAdapter(DisplayAllTransactions.this, helper.GetSpecificIncomeRecords(query));
                displayExpense.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                displayExpense.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayExpense.removeAllViews();

                adapter=new DisplayExpenseRecordsAdapter(DisplayAllTransactions.this, helper.GetSpecificIncomeRecords(newText));
                displayExpense.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                displayExpense.setAdapter(adapter);
                return true;
            }
        });


    }
}