package com.example.Ledgerdiary.offlinecustomer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.onlinecustomer.transactionadepter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ocustomerinterfaceadepter extends RecyclerView.Adapter<ocustomerinterfaceadepter.viewHolder>{

    List<offlinetransactions> list;
    Context context;
    Animation blink;

    public ocustomerinterfaceadepter(List<offlinetransactions> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ocustomerinterfaceadepter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.transactionentry,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ocustomerinterfaceadepter.viewHolder holder, int position) {
      offlinetransactions transaction=list.get(position);
        String datee=transaction.getDate();
        String[] parts = datee.split(" ");
        String day = parts[0];
        String month= parts[1];

        blink= AnimationUtils.loadAnimation(context,R.anim.buttonbehaviour);
        holder.day.setText(day);
        holder.month.setText(month);
        if(transaction.getSender()){
         holder.amount.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
        }else{
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        holder.amount.setText(String.valueOf(transaction.getAmount()));
        holder.description.setText(transaction.getDescription());
        holder.time.setText(transaction.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView time,description,amount,day,month;
        RelativeLayout rl;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.currenttime);
            description=itemView.findViewById(R.id.description);
            amount=itemView.findViewById(R.id.tamount);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
            rl=itemView.findViewById(R.id.rltransactionentry);
        }
    }
}
