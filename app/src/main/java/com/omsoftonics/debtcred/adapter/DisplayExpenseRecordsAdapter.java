package com.omsoftonics.debtcred.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omsoftonics.debtcred.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


public class DisplayExpenseRecordsAdapter extends RecyclerView.Adapter<DisplayExpenseRecordsAdapter.ViewHolder> {

    private AppCompatActivity activity;



    public DisplayExpenseRecordsAdapter(AppCompatActivity context) {
        this.activity = context;
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


            position = currentEventInformation.getExpanditures().size() - 1 - position;

            currentEventInformation.getExpanditures().get(position).setIndex(position);
            viewHolder.date.setText(currentEventInformation.getExpanditures().get(position).getDate());
            viewHolder.expenseBy.setText(currentEventInformation.getExpanditures().get(position).getGivenTo());
            viewHolder.expenseFor.setText(currentEventInformation.getExpanditures().get(position).getGivenFor());
            viewHolder.amount.setText(Integer.toString(currentEventInformation.getExpanditures().get(position).getAmount()));


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
                            currentEventInformation.getExpanditures().get(finalPosition1).RemoveRecord(activity);
                            currentEventInformation.DeleteExpenseRecord(currentEventInformation.getExpanditures().get(finalPosition1));
                            currentEventInformation.UpdateRecord(activity);
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
        if(currentEventInformation.getExpanditures()==null){
            return 0;
        }
        else {
            return currentEventInformation.getExpanditures().size();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView expenseFor;
        private TextView amount;
        private TextView expenseBy;


        private LinearLayout click;



        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.display_expense_date);
            expenseFor = (TextView) v.findViewById(R.id.display_expense_for_what);
            amount = (TextView) v.findViewById(R.id.display_expense_amount);
            click=(LinearLayout)v.findViewById(R.id.display_expense_click);
            expenseBy=(TextView) v.findViewById(R.id.display_expense_personname);
        }
    }
}
