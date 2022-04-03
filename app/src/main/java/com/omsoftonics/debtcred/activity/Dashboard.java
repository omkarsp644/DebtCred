    package com.omsoftonics.debtcred.activity;

    import android.Manifest;
    import android.app.AlertDialog;
    import android.content.ActivityNotFoundException;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.content.res.ColorStateList;
    import android.graphics.Color;
    import android.graphics.Typeface;
    import android.graphics.pdf.PdfDocument;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.text.SpannableString;
    import android.text.style.ForegroundColorSpan;
    import android.text.style.RelativeSizeSpan;
    import android.text.style.StyleSpan;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.github.mikephil.charting.charts.LineChart;
    import com.github.mikephil.charting.charts.PieChart;
    import com.github.mikephil.charting.components.AxisBase;
    import com.github.mikephil.charting.components.XAxis;
    import com.github.mikephil.charting.data.Entry;
    import com.github.mikephil.charting.data.LineData;
    import com.github.mikephil.charting.data.LineDataSet;
    import com.github.mikephil.charting.data.PieData;
    import com.github.mikephil.charting.data.PieDataSet;
    import com.github.mikephil.charting.data.PieEntry;
    import com.github.mikephil.charting.formatter.IAxisValueFormatter;
    import com.github.mikephil.charting.formatter.PercentFormatter;
    import com.github.mikephil.charting.formatter.ValueFormatter;
    import com.github.mikephil.charting.highlight.Highlight;
    import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
    import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
    import com.github.mikephil.charting.utils.ColorTemplate;
    import com.google.android.material.navigation.NavigationView;
    import com.omsoftonics.debtcred.MainActivity;
    import com.omsoftonics.debtcred.R;
    import com.omsoftonics.debtcred.helper.BackupDatabase;
    import com.omsoftonics.debtcred.helper.PrintPDFInformation;
    import com.omsoftonics.debtcred.model.MoneyInformation;
    import com.omsoftonics.debtcred.model.Record;
    import com.omsoftonics.debtcred.sqlitehelper.SqliteDatabaseHelper;

    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.TreeMap;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.cardview.widget.CardView;
    import androidx.core.app.ActivityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import lecho.lib.hellocharts.gesture.ContainerScrollType;
    import lecho.lib.hellocharts.gesture.ZoomType;
    import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
    import lecho.lib.hellocharts.model.Axis;
    import lecho.lib.hellocharts.model.AxisValue;
    import lecho.lib.hellocharts.model.Line;
    import lecho.lib.hellocharts.model.LineChartData;
    import lecho.lib.hellocharts.model.PointValue;
    import lecho.lib.hellocharts.view.LineChartView;

    import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_EXPENSE;
    import static com.omsoftonics.debtcred.MainActivity.RECORD_TYPE_INCOME;
    import static com.omsoftonics.debtcred.MainActivity.currentInformation;


    public class Dashboard extends AppCompatActivity implements LineChartOnValueSelectListener, OnChartValueSelectedListener {


//        LineChartView lineChartView ;
        TextView eventName,income,expense,balance;
        CardView printData;
        LinearLayout displayPreviousTransactions;


        DrawerLayout drawerView;
        NavigationView navigationView;

        private PieChart chart;
        SqliteDatabaseHelper helper;

        LineChart linechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        lineChartView = findViewById(R.id.eventLineChartDash);
//        lineChartView.setOnValueTouchListener(this);
//        lineChartView.setInteractive(true);
//        lineChartView.setZoomType(ZoomType.HORIZONTAL);
//        lineChartView.setPadding(50,10,50,10);
//        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
       // InitializeDataForGraphs();

        helper=new SqliteDatabaseHelper(this);




        drawerView=(DrawerLayout) findViewById(R.id.varganiLayout);

        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray)));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.importItem:
                        BackupDatabase.importDB(Dashboard.this);
                        currentInformation=helper.SetupAllInformation();
                        startActivity(getIntent());
                        finish();
                        break;
                    case R.id.exportItem:

                        BackupDatabase.exportDB(Dashboard.this);
                        break;
                    case R.id.rateApp:

                       RateMyApp();
                        break;


                }
                return true;
            }
        });
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerView.openDrawer(navigationView);
//            }
//        });
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.rateApp:
//                        RateMyApp();
//                        break;
//                }
//                return true;
//            }
//
//        });


    }

        @Override
        protected void onStart() {
            super.onStart();
            eventName=(TextView)findViewById(R.id.eventNameDash);
            income=(TextView)findViewById(R.id.eventIncomeDash);
            balance=(TextView)findViewById(R.id.eventBalanceDash);
            displayPreviousTransactions=(LinearLayout)findViewById(R.id.display_previous_Transactions);


            printData=(CardView)findViewById(R.id.printData);
            expense=(TextView)findViewById(R.id.eventExpenseDash);


            income.setText(Integer.toString(currentInformation.getIncome_Total()));
            expense.setText(Integer.toString(currentInformation.getExpense_Total()));
            balance.setText(Integer.toString((currentInformation.getBalance())));



     
           // InitializeDataForGraphs();


            displayPreviousTransactions.removeAllViews();
            for (int i=currentInformation.getRecord_List().size()-1;i>=0;i--){
                Record rec=currentInformation.getRecord_List().get(i);

                View v=getLayoutInflater().inflate(R.layout.display_previous_trans,null,false);

                TextView date=(TextView) v.findViewById(R.id.displaydate);

                TextView comment=(TextView) v.findViewById(R.id.displayComment);

                TextView amount=(TextView) v.findViewById(R.id.displayAmount);

                ImageView img=(ImageView)v.findViewById(R.id.displayImage);

//                CardView card=v.findViewById(R.id.cardBack);


                if (rec.getRecordType()== MainActivity.RECORD_TYPE_EXPENSE){
                    date.setTextColor(getResources().getColor(R.color.redRec));
                    comment.setTextColor(getResources().getColor(R.color.redRec));
                    amount.setTextColor(getResources().getColor(R.color.redRec));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.expense_new));
//                    card.setBackgroundColor(getResources().getColor(R.color.redRecFaint));
                }
                else{

                    date.setTextColor(getResources().getColor(R.color.greenRec));
                    comment.setTextColor(getResources().getColor(R.color.greenRec));
                    amount.setTextColor(getResources().getColor(R.color.greenRec));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.income_new));

//                    card.setBackgroundColor(getResources().getColor(R.color.greenRecFaint));
                }
                date.setText(rec.getDate());
                comment.setText(rec.getComment());
                amount.setText(""+rec.getAmount());


                displayPreviousTransactions.addView(v);

                SetupPieChart();
                Setuplinechart();

            }



            printData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                    PrintPDFInformation p=new PrintPDFInformation(Dashboard.this);
                    try {
                        if(ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                            p.printToPdf(currentInformation);
                        else {
                            Toast.makeText(Dashboard.this, "Both Permission are required for Pdf Creation", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        private void Setuplinechart() {
            linechart=(LineChart)findViewById(R.id.linechart);

            linechart.setOnChartValueSelectedListener(this);

            linechart.setDrawGridBackground(false);
            linechart.getDescription().setEnabled(false);
            linechart.setDrawBorders(false);

            linechart.getAxisLeft().setEnabled(false);
            linechart.getAxisRight().setDrawAxisLine(false);
            linechart.getAxisRight().setDrawGridLines(false);
            linechart.getXAxis().setDrawAxisLine(false);
            linechart.getXAxis().setDrawGridLines(false);

            // enable touch gestures
            linechart.setTouchEnabled(true);

            // enable scaling and dragging
            linechart.setDragEnabled(true);
            linechart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            linechart.setPinchZoom(false);


            linechart.resetTracking();



            ArrayList<Entry> incomeD=new ArrayList<Entry>();
            ArrayList<Entry> expenseD=new ArrayList<Entry>();
            ArrayList<String> dateD=new ArrayList<String>();


            class DisplayIncomeExpensePerDay{
                int Income;
                int Expense;
                String Date;

                public DisplayIncomeExpensePerDay(int income, int expense, String date) {
                    Income = income;
                    Expense = expense;
                    Date = date;
                }

                public DisplayIncomeExpensePerDay(String Date) {
                    this.Income=0;
                    this.Expense=0;
                    this.Date=Date;
                }
            }


            HashMap<String, DisplayIncomeExpensePerDay> df = new HashMap<String, DisplayIncomeExpensePerDay>();
            SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");

            for (Record v: currentInformation.getRecord_List()) {
                try {

                    DisplayIncomeExpensePerDay demo=null;
                    //Date d=er.parse(v.getDate());
                    if(df.containsKey(v.getDate())){
                        demo=df.get(v.getDate());
                    }
                    else{
                        demo= new DisplayIncomeExpensePerDay(v.getDate());
                    }

                    if (v.getRecordType()==RECORD_TYPE_EXPENSE){
                        demo.Expense+=v.getAmount();
                    }
                    if (v.getRecordType()==RECORD_TYPE_INCOME){
                        demo.Income+=v.getAmount();
                    }


                    df.put( v.getDate(), demo);

                } catch (Exception e) {

                }
            }

            int count=0;
            dateD.add("start");
            incomeD.add(new Entry(count,0));
            expenseD.add(new Entry(count,0));
            count++;

            for (Map.Entry<String, DisplayIncomeExpensePerDay> v : df.entrySet())
            {
                dateD.add(v.getKey());
                DisplayIncomeExpensePerDay ob=df.get(v.getKey());

                incomeD.add(new Entry(count,ob.Income));
                expenseD.add(new Entry(count,ob.Expense));
                count++;

            }


            LineDataSet dataSetIncome = new LineDataSet(incomeD, "Income");
            dataSetIncome.setColor(this.getResources().getColor(R.color.greenRec));
            dataSetIncome.setCircleColor(this.getResources().getColor(R.color.greenRec));
            LineDataSet dataSetExpense = new LineDataSet(expenseD, "Expense");
            dataSetExpense.setColor(this.getResources().getColor(R.color.redRec));
            dataSetExpense.setCircleColor(this.getResources().getColor(R.color.redRec));

            List<ILineDataSet> lines = new ArrayList<ILineDataSet>();
            lines.add(dataSetIncome);
            lines.add(dataSetExpense);


            LineData data = new LineData(lines);
            linechart.setData(data);





            linechart.invalidate();


        }

        private void RateMyApp() {
            try
            {
                Intent rateIntent = rateIntentForUrl("market://details");
                startActivity(rateIntent);
            }
            catch (ActivityNotFoundException e)
            {
                Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
                startActivity(rateIntent);
            }

        }

        private Intent rateIntentForUrl(String url)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
            int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
            if (Build.VERSION.SDK_INT >= 21)
            {
                flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
            }
            else
            {
                //noinspection deprecation
                flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
            }
            intent.addFlags(flags);
            return intent;
        }



        private void SetupPieChart() {
            chart = findViewById(R.id.chart1);
            chart.setUsePercentValues(true);
            chart.getDescription().setEnabled(false);
            chart.setExtraOffsets(5, 10, 5, 5);

            chart.setDragDecelerationFrictionCoef(0.95f);


            chart.setDrawHoleEnabled(true);
            chart.setHoleColor(Color.WHITE);

            chart.setTransparentCircleColor(Color.WHITE);
            chart.setTransparentCircleAlpha(110);

            chart.setHoleRadius(50f);
            chart.setTransparentCircleRadius(50f);

            chart.setDrawCenterText(true);

            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);
            chart.setHighlightPerTapEnabled(true);

            //chart.setUnit(" ");
            // chart.setDrawUnitsInChart(true);

            // add a selection listener
            chart.setOnChartValueSelectedListener(this);

            showPieChart();

        }

        private void showPieChart(){

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "Debt Cred";

            //initializing data
            Map<String, Integer> typeAmountMap = new HashMap<>();
            typeAmountMap.put("Income",currentInformation.getIncome_Total());
            typeAmountMap.put("Expense",currentInformation.getExpense_Total());
            typeAmountMap.put("Savings",currentInformation.getIncome_Total()-currentInformation.getExpense_Total());

            //initializing colors for the entries
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#0b2d39"));
            colors.add(Color.parseColor("#be4d25"));
            colors.add(Color.parseColor("#2596be"));


            //input data and fit data into pie chart entry
            for(String type: typeAmountMap.keySet()){
                pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
            //setting text size of the value
            pieDataSet.setValueTextSize(10f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);
            pieData.setValueFormatter(new PercentFormatter());

            chart.setData(pieData);
            chart.invalidate();
        }

        public void AddVargani(View view) {
        startActivity(new Intent(Dashboard.this,Registervargani.class));
        }

        public void AddExpense(View view) {
            startActivity(new Intent(Dashboard.this,RegisterExpense.class));
        }

        public void GetReports(View view) {
            startActivity(new Intent(Dashboard.this,GenerateReport.class));
        }

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getApplicationContext(),""+value.getY(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

        public void Reset_All(View view) {

            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle(R.string.reset_application);
            alert.setMessage(R.string.reset_application_message);
            alert.setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ClearData();
                }
            });
            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.create().show();

        }

        private void ClearData() {
            currentInformation.ResetAllData(this);
            currentInformation=helper.SetupAllInformation();
            startActivity(getIntent());
            finish();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.data_removed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {


        }


        @Override
        public void onValueSelected(Entry e, Highlight h) {

            if (e == null)
                return;
            Log.i("VAL SELECTED",
                    "Value: " + e.getY() + ", index: " + h.getX()
                            + ", DataSet index: " + h.getDataSetIndex());
        }

        @Override
        public void onNothingSelected() {
            Log.i("PieChart", "nothing selected");
        }

        public void ShowAllTransactions(View view) {
        startActivity(new Intent(Dashboard.this,DisplayAllTransactions.class));
        }

        public void openMenu(View view) {
            drawerView.openDrawer(navigationView);
        }
    }