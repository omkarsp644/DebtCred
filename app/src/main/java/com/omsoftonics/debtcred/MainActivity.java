package com.omsoftonics.debtcred;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.omsoftonics.debtcred.activity.Dashboard;
import com.omsoftonics.debtcred.model.MoneyInformation;
import com.omsoftonics.debtcred.model.Record;
import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //https://app.flycricket.com/app/ad35d8e385cb4bd8b42f3982901f5af0/screenshots

    public static MoneyInformation currentInformation=null;

    public static ArrayList<Record> RecordList=new ArrayList<>();


    public static final int RECORD_TYPE_INCOME=1;

    public static final int RECORD_TYPE_EXPENSE=2;


    public static File BACKUP_IMPORT_EXPORT = new File(Environment.getExternalStorageDirectory() + File.separator + "DebtCredManagerBackup/DebtCredManagerfile.csv");


    public static int REQUEST_CODE_PERMISSIONS=234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SqliteDatabaseHelper helper=new SqliteDatabaseHelper(this);

      currentInformation=helper.SetupAllInformation();


        new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(com.omsoftonics.debtcred.MainActivity.this, Dashboard.class));
                    finish();
                }
            }
        }.start();
    }
}