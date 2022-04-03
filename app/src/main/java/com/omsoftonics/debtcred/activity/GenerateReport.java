package com.omsoftonics.debtcred.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;


public class GenerateReport extends AppCompatActivity implements OnChartValueSelectedListener {

    TextView incomeReport,expenseReport,pendingReport;

    LinearLayout displayVargani,displayExpense,displayDayWise,displayMonthWise,displayYearWise;

    ScrollView print;
    Bitmap bitmap;

   //LineChartView lineChartView ;'
   private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);


    }
    @Override
    protected void onStart() {
        super.onStart();

        incomeReport=(TextView)findViewById(R.id.incomeAnalysis);
        expenseReport=(TextView)findViewById(R.id.expenseAnalysis);
        pendingReport=(TextView)findViewById(R.id.balancemoney);

        displayExpense=(LinearLayout)findViewById(R.id.displayExpenseReport);
        displayVargani=(LinearLayout)findViewById(R.id.displayVarganiReport);





        incomeReport.setText(Integer.toString(currentInformation.getIncome_Total()));
        expenseReport.setText(Integer.toString(currentInformation.getExpense_Total()));
        pendingReport.setText(""+(currentInformation.getIncome_Total()-currentInformation.getExpense_Total()));

        displayVargani.removeAllViews();
        int count=1;
        for (Record v : new ArrayList<Record>(currentInformation.getRecord_List().stream().filter(p->p.getRecordType()==MainActivity.RECORD_TYPE_INCOME).collect(Collectors.toList()))){
            View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);

            LinearLayout d=view.findViewById(R.id.display_click);


                d.setBackgroundColor(getResources().getColor(R.color.greenDisplay));

            TextView j=(TextView)view.findViewById(R.id.record_date);
            j.setText(v.getDate());

            TextView k=(TextView)view.findViewById(R.id.record_comment);
            k.setText(v.getComment());

            TextView l=(TextView)view.findViewById(R.id.record_amount);
            l.setText(""+v.getAmount());

            displayVargani.addView(view);
        }


        displayExpense.removeAllViews();
        //displayExpense.setVisibility(View.GONE);
        for (Record v : new ArrayList<Record>(currentInformation.getRecord_List().stream().filter(p->p.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE).collect(Collectors.toList()))){
            View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);

            LinearLayout d=view.findViewById(R.id.display_click);


            d.setBackgroundColor(getResources().getColor(R.color.redDisplay));

            TextView j=(TextView)view.findViewById(R.id.record_date);
            j.setText(v.getDate());

            TextView k=(TextView)view.findViewById(R.id.record_comment);
            k.setText(v.getComment());

            TextView l=(TextView)view.findViewById(R.id.record_amount);
            l.setText(""+v.getAmount());
            displayExpense.addView(view);
        }


       // InitializeDataForGraphs();



        displayDayWise=(LinearLayout)findViewById(R.id.displayDayWise);
        displayMonthWise=(LinearLayout)findViewById(R.id.displayMonthWise);
        displayYearWise=(LinearLayout)findViewById(R.id.displayYearWise);

        InitializeDayWiseCollection();
        InitializeMonthWiseCollection();
        InitializeYearWiseCollection();


        SetupPieChart();


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
    //https://www.codingdemos.com/

    private void InitializeDayWiseCollection() {
        Map<Date, ArrayList<Record>> m = new HashMap<Date, ArrayList<Record>>();
        SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
        for (Record v: currentInformation.getRecord_List()) {
            try {
                //if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE) {
                Date d = er.parse(v.getDate());
                if (!m.containsKey(d)) {
                    m.put(d, new ArrayList<>());
                }
                ArrayList<Record> demo=m.get(d);
                demo.add(v);
                m.put(d, demo);

                //}
            } catch (Exception e) {
            }
        }
        Map<Date, ArrayList<Record>> m1 = new TreeMap(m);
        ArrayList<String> dates=new ArrayList<>();
        ArrayList<ArrayList<Record>> reciepts=new ArrayList<>();
        for (Map.Entry<Date, ArrayList<Record>> entry : m1.entrySet())
        {
            dates.add(er.format(entry.getKey()));
            reciepts.add(entry.getValue());
        }

        for (int i=0;i<dates.size();i++){


            int totalincome=0;
            int totalexpense=0;
            String date=dates.get(i);

            ArrayList<Record> v11=reciepts.get(i);

            View dateBox=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView dat=(TextView)dateBox.findViewById(R.id.DateDisplay);
            dat.setText(date);
            displayDayWise.addView(dateBox);


            View header=getLayoutInflater().inflate(R.layout.vargani_headers,null,false);
            displayDayWise.addView(header);

            for (Record v:v11){

                View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);

                LinearLayout d=view.findViewById(R.id.display_click);


                if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE){

                    totalexpense+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.redDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayDayWise.addView(view);
                }
                else{
                    totalincome+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.greenDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayDayWise.addView(view);
                }
            }

            View amount=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt=(TextView)amount.findViewById(R.id.DateDisplay);
            amt.setTextSize(20);
            amt.setText("Expense : "+totalexpense);


            View amount1=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt1=(TextView)amount1.findViewById(R.id.DateDisplay);
            amt1.setTextSize(20);
            amt1.setText("Income : "+totalincome);

            //

            displayDayWise.addView(amount);
            displayDayWise.addView(amount1);



            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            displayDayWise.addView(v);

        }


    }

    private void InitializeMonthWiseCollection() {
        Map<String, ArrayList<Record>> m = new HashMap<String, ArrayList<Record>>();
        //SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
        for (Record v: currentInformation.getRecord_List()) {
            try {
                //if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE) {
//                Date d = er.parse(v.getDate());

                String d=v.getDate().split("/")[1]+"-"+v.getDate().split("/")[2];
                if (!m.containsKey(d)) {
                    m.put(d, new ArrayList<>());
                }
                ArrayList<Record> demo=m.get(d);
                demo.add(v);
                m.put(d, demo);

                //}
            } catch (Exception e) {
            }
        }
        Map<String, ArrayList<Record>> m1 = new TreeMap(m);
        ArrayList<String> dates=new ArrayList<>();
        ArrayList<ArrayList<Record>> reciepts=new ArrayList<>();
        for (Map.Entry<String, ArrayList<Record>> entry : m1.entrySet())
        {
            dates.add(entry.getKey());
            reciepts.add(entry.getValue());
        }

        for (int i=0;i<dates.size();i++){


            int totalincome=0;
            int totalexpense=0;
            String date=dates.get(i);

            ArrayList<Record> v11=reciepts.get(i);

            View dateBox=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView dat=(TextView)dateBox.findViewById(R.id.DateDisplay);
            dat.setText(date);
            displayMonthWise.addView(dateBox);


            View header=getLayoutInflater().inflate(R.layout.vargani_headers,null,false);
            displayMonthWise.addView(header);

            for (Record v:v11){

                View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);

                LinearLayout d=view.findViewById(R.id.display_click);


                if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE){

                    totalexpense+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.redDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayMonthWise.addView(view);
                }
                else{
                    totalincome+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.greenDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayMonthWise.addView(view);
                }
            }

            View amount=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt=(TextView)amount.findViewById(R.id.DateDisplay);
            amt.setTextSize(20);
            amt.setText("Expense : "+totalexpense);


            View amount1=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt1=(TextView)amount1.findViewById(R.id.DateDisplay);
            amt1.setTextSize(20);
            amt1.setText("Income : "+totalincome);

            //

            displayMonthWise.addView(amount);
            displayMonthWise.addView(amount1);



            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            displayMonthWise.addView(v);

        }


    }

    private void InitializeYearWiseCollection() {
        Map<String, ArrayList<Record>> m = new HashMap<String, ArrayList<Record>>();
        SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
        for (Record v: currentInformation.getRecord_List()) {
            try {
                //if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE) {
//                Date d = er.parse(v.getDate());
                String s=v.getDate().split("/")[2];
                if (!m.containsKey(s)) {
                    m.put(s, new ArrayList<>());
                }
                ArrayList<Record> demo=m.get(s);
                demo.add(v);
                m.put(s, demo);

                //}
            } catch (Exception e) {
            }
        }
        Map<String, ArrayList<Record>> m1 = new TreeMap(m);
        ArrayList<String> dates=new ArrayList<>();
        ArrayList<ArrayList<Record>> reciepts=new ArrayList<>();
        for (Map.Entry<String, ArrayList<Record>> entry : m1.entrySet())
        {
            dates.add(entry.getKey());
            reciepts.add(entry.getValue());
        }

        for (int i=0;i<dates.size();i++){


            int totalincome=0;
            int totalexpense=0;
            String date=dates.get(i);

            ArrayList<Record> v11=reciepts.get(i);

            View dateBox=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView dat=(TextView)dateBox.findViewById(R.id.DateDisplay);
            dat.setText(date);
            displayYearWise.addView(dateBox);


            View header=getLayoutInflater().inflate(R.layout.vargani_headers,null,false);
            displayYearWise.addView(header);

            for (Record v:v11){

                View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);

                LinearLayout d=view.findViewById(R.id.display_click);


                if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE){

                    totalexpense+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.redDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayYearWise.addView(view);
                }
                else{
                    totalincome+=v.getAmount();
                    d.setBackgroundColor(getResources().getColor(R.color.greenDisplay));

                    TextView j=(TextView)view.findViewById(R.id.record_date);
                    j.setText(v.getDate());

                    TextView k=(TextView)view.findViewById(R.id.record_comment);
                    k.setText(v.getComment());

                    TextView l=(TextView)view.findViewById(R.id.record_amount);
                    l.setText(""+v.getAmount());


                    displayYearWise.addView(view);
                }
            }

            View amount=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt=(TextView)amount.findViewById(R.id.DateDisplay);
            amt.setTextSize(20);
            amt.setText("Expense : "+totalexpense);


            View amount1=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt1=(TextView)amount1.findViewById(R.id.DateDisplay);
            amt1.setTextSize(20);
            amt1.setText("Income : "+totalincome);

            //

            displayYearWise.addView(amount);
            displayYearWise.addView(amount1);



            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            displayYearWise.addView(v);

        }


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}