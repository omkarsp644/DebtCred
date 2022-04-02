package com.omsoftonics.debtcred.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.adapter.DisplayVarganiRecordsAdapter;
import com.omsoftonics.debtcred.model.Record;
import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;
import static com.omsoftonics.debtcred.model.MoneyInformation.GetCurrentdate;


public class Registervargani extends AppCompatActivity {

    EditText comment,amonut;
    TextView todaysDate,registerVargani;

    DisplayVarganiRecordsAdapter adapter;
    RecyclerView displayVarganiRecords;

    public static boolean isFromRecord=false;
    public static int indexValue=0;
    SqliteDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vargani);

        helper=new SqliteDatabaseHelper(this);
        adapter=new DisplayVarganiRecordsAdapter(Registervargani.this,currentInformation.getRecord_List());
        displayVarganiRecords=(RecyclerView)findViewById(R.id.displayVarganiRecords);

        try {
            displayVarganiRecords.setAdapter(adapter);
        }
        catch (Exception e){

        }


        SearchView searchView=(SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displayVarganiRecords.removeAllViews();

                adapter=new DisplayVarganiRecordsAdapter(Registervargani.this, helper.GetSpecificIncomeRecords(query));
                displayVarganiRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                displayVarganiRecords.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayVarganiRecords.removeAllViews();

                adapter=new DisplayVarganiRecordsAdapter(Registervargani.this, helper.GetSpecificIncomeRecords(newText));
                displayVarganiRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                displayVarganiRecords.setAdapter(adapter);

                return true;
            }
        });


        todaysDate=(TextView)findViewById(R.id.currentDate);
        comment=(EditText)findViewById(R.id.income_name);
        amonut=(EditText)findViewById(R.id.income_amountPaid);
        registerVargani=(TextView)findViewById(R.id.registerVargani);
        todaysDate.setText(GetCurrentdate());

        registerVargani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVarganiM();
            }
        });
    }

    private void registerVarganiM() {




        if(comment.getText().length()==0){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.payer_name_required), Toast.LENGTH_SHORT).show();
            comment.setError(getResources().getString(R.string.payer_name_required));
            return;
        }

        if(amonut.getText().toString().equals("") || amonut.getText().toString()==null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.amount_must_greator_than_zero), Toast.LENGTH_SHORT).show();
            amonut.setError(getResources().getString(R.string.amount_must_greator_than_zero));
            return;
        }
        if(!amonut.getText().toString().equals("") && Integer.parseInt(amonut.getText().toString())<=0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.amount_must_greator_than_zero), Toast.LENGTH_SHORT).show();
            amonut.setError(getResources().getString(R.string.amount_must_greator_than_zero));
            return;
        }


         Record reciepts=new Record();


        reciepts.setAmount(Integer.parseInt(amonut.getText().toString()));
        reciepts.setDate(todaysDate.getText().toString());
        reciepts.setComment(comment.getText().toString());
        reciepts.setRecordType(MainActivity.RECORD_TYPE_INCOME);
        reciepts.setArrayIndex(currentInformation.getRecord_List().size());




            if (!isFromRecord) {
                reciepts.SaveRecord(this);
                currentInformation.AddRecord(reciepts);

            } else {

                isFromRecord = false;
                currentInformation.UpdateRecord(reciepts);
                reciepts.UpdateRecord(this);
            }

            currentInformation.UpdateRecord(reciepts);




        comment.getText().clear();
        amonut.getText().clear();

        adapter.notifyDataSetChanged();

        adapter=new DisplayVarganiRecordsAdapter(Registervargani.this,currentInformation.getRecord_List());
        displayVarganiRecords=(RecyclerView)findViewById(R.id.displayVarganiRecords);

        try {
            displayVarganiRecords.setAdapter(adapter);
        }
        catch (Exception e){

        }
    }



    private void setDateTimeField(Calendar c, TextView v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy");
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth, 0, 0);
                v.setText(dateFormatter.format(c.getTime()));
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}