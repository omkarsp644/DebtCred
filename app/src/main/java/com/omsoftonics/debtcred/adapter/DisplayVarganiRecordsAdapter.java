package com.omsoftonics.debtcred.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.model.Record;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;
import static com.omsoftonics.debtcred.activity.Registervargani.indexValue;
import static com.omsoftonics.debtcred.activity.Registervargani.isFromRecord;

public class DisplayVarganiRecordsAdapter extends RecyclerView.Adapter<DisplayVarganiRecordsAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private ArrayList<Record> varganireciepts;


    public DisplayVarganiRecordsAdapter(AppCompatActivity context, ArrayList<Record> varga) {
        this.activity = context;
        this.varganireciepts=varga;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.display_expense, parent, false);


        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {




        position=varganireciepts.size()-1-position;

        varganireciepts.get(position).setArrayIndex(position);

        if (this.varganireciepts.get(position).getRecordType()== MainActivity.RECORD_TYPE_INCOME) {
            viewHolder.click.setBackgroundColor(activity.getResources().getColor(R.color.greenDisplay));
        }
        else{

            viewHolder.click.setBackgroundColor(activity.getResources().getColor(R.color.redDisplay));
        }

        viewHolder.name.setText(varganireciepts.get(position).getComment());
        viewHolder.amount.setText(Integer.toString(varganireciepts.get(position).getAmount()));
        viewHolder.date.setText(varganireciepts.get(position).getDate());



        int finalPosition = position;
        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(activity,"ID :"+varganireciepts.get(finalPosition).getArrayIndex()+"   "+varganireciepts.get(finalPosition), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert=new AlertDialog.Builder(activity);
                alert.setTitle("Want to Edit or Delete...?");
                alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditValues(finalPosition);
                    }
                });
                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.create().show();
            }
        });


        int finalPosition1 = position;
        viewHolder.click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(activity);
                alert.setTitle(R.string.want_to_delete);
                alert.setMessage(R.string.once_deleted);
                alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentInformation.UpdateRecord(varganireciepts.get(finalPosition));
                        varganireciepts.get(finalPosition1).RemoveRecord(activity);
                        currentInformation.DeleteRecord(varganireciepts.get(finalPosition1));

                        notifyDataSetChanged();

                    }
                });

                alert.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.setCancelable(false);
                alert.create().show();


                return true;
            }
        });


    }

    private void EditValues(int finalPosition) {
        TextView currentTime=(TextView) activity.findViewById(R.id.currentDate);
        currentTime.setText(varganireciepts.get(finalPosition).getDate());



        EditText name=(EditText)activity.findViewById(R.id.income_name);
        name.setText(varganireciepts.get(finalPosition).getComment());


        EditText amount=(EditText)activity.findViewById(R.id.income_amount);
        amount.setText(Integer.toString(varganireciepts.get(finalPosition).getAmount()));

        isFromRecord=true;

        indexValue=varganireciepts.get(finalPosition).getArrayIndex();
    }

    @Override
    public int getItemCount() {
        if(varganireciepts==null){
            return 0;
        }
        else {
            return varganireciepts.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView amount;
        private TextView date;

        private LinearLayout click;



        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.record_comment);
            amount = (TextView) v.findViewById(R.id.record_amount);
            click=(LinearLayout)v.findViewById(R.id.display_click);

            date=(TextView)v.findViewById(R.id.record_date);

        }
    }
}
