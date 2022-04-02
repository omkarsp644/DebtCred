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


public class GenerateReport extends AppCompatActivity implements LineChartOnValueSelectListener {

    TextView incomeReport,expenseReport,pendingReport;

    LinearLayout displayVargani,displayExpense,displayDayWise;

    ScrollView print;
    Bitmap bitmap;

    LineChartView lineChartView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        lineChartView = findViewById(R.id.eventLineChartDash);
        
    }



    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected void onStart() {
        super.onStart();

        incomeReport=(TextView)findViewById(R.id.incomeAnalysis);
        expenseReport=(TextView)findViewById(R.id.expenseAnalysis);
        pendingReport=(TextView)findViewById(R.id.pendingAnalysis);

        displayExpense=(LinearLayout)findViewById(R.id.displayExpenseReport);
        displayVargani=(LinearLayout)findViewById(R.id.displayVarganiReport);





        incomeReport.setText(Integer.toString(currentInformation.getIncome_Total()));
        expenseReport.setText(Integer.toString(currentInformation.getExpense_Total()));


        displayVargani.removeAllViews();
        int count=1;
        for (Record v : currentInformation.getRecord_List()){
            View view=getLayoutInflater().inflate(R.layout.display_vargani_items,null,false);

            LinearLayout d=view.findViewById(R.id.clickItem);

            boolean isExp=true;
            if(v.getRecordType()== MainActivity.RECORD_TYPE_EXPENSE){
                d.setBackgroundColor(getResources().getColor(R.color.redDisplay));
                isExp=true;
            }
            else{

                isExp=false;
                d.setBackgroundColor(getResources().getColor(R.color.greenDisplay));
            }
            TextView i=(TextView)view.findViewById(R.id.reciept_ID);
            i.setText(Integer.toString(v.getDatabaseID()));

            TextView j=(TextView)view.findViewById(R.id.reciept_comment);
            j.setText(v.getComment());

            TextView k=(TextView)view.findViewById(R.id.reciept_date);
            k.setText(v.getDate());

            TextView l=(TextView)view.findViewById(R.id.reciept_income);
            l.setText(""+(isExp?v.getAmount():0));

            TextView m=(TextView)view.findViewById(R.id.reciept_expense);
            m.setText(""+(isExp?v.getAmount():0));
            displayVargani.addView(view);
        }


        displayExpense.removeAllViews();
        displayExpense.setVisibility(View.GONE);
//        for (Expanditures v : currentEventInformation.getExpanditures()){
//            View view=getLayoutInflater().inflate(R.layout.display_expense,null,false);
//
//            TextView i=(TextView)view.findViewById(R.id.display_expense_date);
//            i.setText(v.getDate());
//
//            TextView j=(TextView)view.findViewById(R.id.display_expense_for_what);
//            j.setText(v.getGivenFor());
//
//            TextView k=(TextView)view.findViewById(R.id.display_expense_amount);
//            k.setText(Integer.toString(v.getAmount()));
//
//            TextView l=(TextView)view.findViewById(R.id.display_expense_personname);
//            l.setText(v.getGivenTo());
//            displayExpense.addView(view);
//        }


       // InitializeDataForGraphs();



        displayDayWise=(LinearLayout)findViewById(R.id.displayDayWise);



        InitializeDayWiseCollection();


    }

    private void InitializeDayWiseCollection() {
        Map<Date, ArrayList<Record>> m = new HashMap<Date, ArrayList<Record>>();
        SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
        for (Record v: currentInformation.getRecord_List()) {
            try {
                if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE) {
                    Date d = er.parse(v.getDate());
                    if (!m.containsKey(d)) {
                        m.put(d, new ArrayList<>());
                    }
                    ArrayList<Record> demo=m.get(d);
                    demo.add(v);
                    m.put(d, demo);

                }
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


            int totalCollection=0;
            String date=dates.get(i);

            ArrayList<Record> v11=reciepts.get(i);

            View dateBox=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView dat=(TextView)dateBox.findViewById(R.id.DateDisplay);
            dat.setText(date);
            displayDayWise.addView(dateBox);


            View header=getLayoutInflater().inflate(R.layout.vargani_headers,null,false);
            displayDayWise.addView(header);

            for (Record v:v11){

                totalCollection+=v.getAmount();
                View view=getLayoutInflater().inflate(R.layout.display_vargani_items,null,false);

                LinearLayout d=view.findViewById(R.id.clickItem);


                if(v.getRecordType()==MainActivity.RECORD_TYPE_EXPENSE){
                    d.setBackgroundColor(getResources().getColor(R.color.redDisplay));

                    TextView i1=(TextView)view.findViewById(R.id.reciept_ID);
                    i1.setText(Integer.toString(v.getDatabaseID()));

                    TextView j=(TextView)view.findViewById(R.id.reciept_comment);
                    j.setText(v.getComment());

                    TextView k=(TextView)view.findViewById(R.id.reciept_date);
                    k.setText(Integer.toString(v.getAmount()));

                    TextView l=(TextView)view.findViewById(R.id.reciept_income);
                    l.setText(0);

                    TextView m11=(TextView)view.findViewById(R.id.reciept_expense);
                    m11.setText(v.getAmount());



                    displayDayWise.addView(view);
                }
            }

            View amount=getLayoutInflater().inflate(R.layout.textboxlayout,null,false);
            TextView amt=(TextView)amount.findViewById(R.id.DateDisplay);
            amt.setTextSize(20);
            amt.setText("Collected : "+totalCollection);

            displayDayWise.addView(amount);


            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            displayDayWise.addView(v);

        }


    }

//    private void InitializeDataForGraphs() {
//        lineChartView = findViewById(R.id.chartVarganiReport);
//
//        lineChartView.setOnValueTouchListener(this);
//        lineChartView.setInteractive(true);
//        lineChartView.setZoomType(ZoomType.HORIZONTAL);
//        lineChartView.setPadding(50,10,50,10);
//        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//
//
//
//
//        Map<Date, Integer> m = new HashMap<Date, Integer>();
//        SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
//        for (Record v: currentEventInformation.getVarganiList()) {
//            try {
//                Date d=er.parse(v.getDateCreated());
//                if(!m.containsKey(d)){
//                    m.put( d, 0);
//                }
//                m.put( d, m.get(d) + v.getAmount());
//            } catch (Exception e) {
//            }
//        }
//        Map<Date, Integer> m1 = new TreeMap(m);
//        ArrayList<String> xAxis=new ArrayList<>();
//        ArrayList<Integer> yAxis=new ArrayList<>();
//        for (Map.Entry<Date, Integer> entry : m1.entrySet())
//        {
//            xAxis.add(er.format(entry.getKey()));
//            yAxis.add(entry.getValue());
//        }
//
//
//        CreateGraph(xAxis.toArray(new String[0]),yAxis.toArray(new Integer[0]));
//
//    }

//    private void CreateGraph(String[] axisData, Integer[] yAxisData) {
//        List yAxisValues = new ArrayList();
//        List axisValues = new ArrayList();
//        Line line = new Line(yAxisValues);
//        for(int i = 0; i < axisData.length; i++){
//            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
//        }
//        for (int i = 0; i < yAxisData.length; i++){
//            yAxisValues.add(new PointValue(i, yAxisData[i]));
//        }
//        List lines = new ArrayList();
//        lines.add(line);
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//        lineChartView.setLineChartData(data);
//        Axis axis = new Axis();
//        axis.setValues(axisValues);
//        axis.setTextColor(getResources().getColor(android.R.color.black));
//        axis.setTextSize(10);
//
//        Axis yAxis = new Axis();
//        yAxis.setTextSize(10);
//        yAxis.setTextColor(getResources().getColor(android.R.color.black));
//        data.setAxisYLeft(yAxis);
//
//        data.setAxisXBottom(axis);
//
//    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        Toast.makeText(getApplicationContext(),""+value.getY(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueDeselected() {

    }
}