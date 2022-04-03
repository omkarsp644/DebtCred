package com.omsoftonics.debtcred.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omsoftonics.debtcred.MainActivity;
import com.omsoftonics.debtcred.R;
import com.omsoftonics.debtcred.model.Record;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.omsoftonics.debtcred.MainActivity.currentInformation;


public class DisplayExpenseRecordsAdapter extends RecyclerView.Adapter<DisplayExpenseRecordsAdapter.ViewHolder> {

    private AppCompatActivity activity;

    private ArrayList<Record> expenseRecords;


    public DisplayExpenseRecordsAdapter(AppCompatActivity context,ArrayList<Record> expe) {
        this.activity = context;
        this.expenseRecords=expe;
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


//            position = this.expenseRecords.size() - 1 - position;

            this.expenseRecords.get(position).setArrayIndex(position);

            if (this.expenseRecords.get(position).getRecordType()== MainActivity.RECORD_TYPE_INCOME) {
                viewHolder.click.setBackgroundColor(activity.getResources().getColor(R.color.greenDisplay));
            }
            else{

                viewHolder.click.setBackgroundColor(activity.getResources().getColor(R.color.redDisplay));
            }
            viewHolder.date.setText(this.expenseRecords.get(position).getDate());
            viewHolder.expenseFor.setText(this.expenseRecords.get(position).getComment());
            viewHolder.amount.setText(Integer.toString(this.expenseRecords.get(position).getAmount()));




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

                            currentInformation.UpdateRecord(expenseRecords.get(finalPosition1));
                            expenseRecords.get(finalPosition1).RemoveRecord(activity);
                            currentInformation.DeleteRecord(expenseRecords.get(finalPosition1));
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

    @Override
    public int getItemCount() {
        if(this.expenseRecords==null){
            return 0;
        }
        else {
            return this.expenseRecords.size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView expenseFor;
        private TextView amount;


        private LinearLayout click;



        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.record_date);
            expenseFor = (TextView) v.findViewById(R.id.record_comment);
            amount = (TextView) v.findViewById(R.id.record_amount);
            click=(LinearLayout)v.findViewById(R.id.display_click);
        }
    }
}
