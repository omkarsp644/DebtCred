    package com.omsoftonics.debtcred.activity;

    import android.Manifest;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.omsoftonics.debtcred.MainActivity;
    import com.omsoftonics.debtcred.R;
    import com.omsoftonics.debtcred.helper.PrintPDFInformation;
    import com.omsoftonics.debtcred.model.MoneyInformation;
    import com.omsoftonics.debtcred.model.Record;

    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.TreeMap;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.cardview.widget.CardView;
    import androidx.core.app.ActivityCompat;
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


    public class Dashboard extends AppCompatActivity implements LineChartOnValueSelectListener {


        LineChartView lineChartView ;
        TextView eventName,income,expense,balance;
        CardView printData;
        LinearLayout displayPreviousTransactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        lineChartView = findViewById(R.id.eventLineChartDash);
        lineChartView.setOnValueTouchListener(this);
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setPadding(50,10,50,10);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
       // InitializeDataForGraphs();




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
        //https://www.codingdemos.com/draw-android-line-chart/

        private void CreateGraph(String[] axisData, Integer[] yAxisData) {
            List yAxisValues = new ArrayList();
            List axisValues = new ArrayList();
            Line line = new Line(yAxisValues);
            for(int i = 0; i < axisData.length; i++){
                axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
            }
            for (int i = 0; i < yAxisData.length; i++){
                yAxisValues.add(new PointValue(i, yAxisData[i]));
            }
            List lines = new ArrayList();
            lines.add(line);
            LineChartData data = new LineChartData();
            data.setLines(lines);
            lineChartView.setLineChartData(data);
            Axis axis = new Axis();
            axis.setValues(axisValues);
            axis.setTextColor(getResources().getColor(android.R.color.black));
            axis.setTextSize(10);
            data.setAxisXBottom(axis);

        }

//        private void InitializeDataForGraphs() {
//
//            Map<Date, Integer> m = new HashMap<Date, Integer>();
//            SimpleDateFormat er = new SimpleDateFormat("dd/MM/yyyy");
//                for (Record v: currentInformation.getRecord_List()) {
//                try {
//
//                        Date d = er.parse(v.getDatePaid());
//                        if (!m.containsKey(d)) {
//                            m.put(d, 0);
//                        }
//
//                        m.put(d, m.get(d) + v.getAmount());
//
//                } catch (Exception e) {
//                }
//            }
//            Map<Date, Integer> m1 = new TreeMap(m);
//            ArrayList<String> xAxis=new ArrayList<>();
//            ArrayList<Integer> yAxis=new ArrayList<>();
//            for (Map.Entry<Date, Integer> entry : m1.entrySet())
//            {
//                xAxis.add(er.format(entry.getKey()));
//                yAxis.add(entry.getValue());
//            }
//
//
//            CreateGraph(xAxis.toArray(new String[0]),yAxis.toArray(new Integer[0]));
//
//
//        }



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
            onBackPressed();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.data_removed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }
    }