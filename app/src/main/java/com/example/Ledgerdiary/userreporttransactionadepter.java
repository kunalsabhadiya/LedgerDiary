package com.example.Ledgerdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.onlinecustomer.transactionadepter;
import com.example.Ledgerdiary.onlinecustomer.transactionmodel;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class userreporttransactionadepter extends RecyclerView.Adapter<userreporttransactionadepter.VireHolder> {
    ArrayList<transactionmodel> list;
    Context context;

    public userreporttransactionadepter(ArrayList<transactionmodel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public userreporttransactionadepter.VireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.transactionentry,parent,false);
        return new VireHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userreporttransactionadepter.VireHolder holder, int position) {
        transactionmodel model=list.get(position);
        String datee=model.getDate();
        String[] parts = datee.split(" ");
        String day = parts[0];
        String month= parts[1];
        Date date=new Date(model.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = dateFormat.format(date);
        holder.day.setText(day);
        holder.month.setText(month);
        if(Objects.equals(FirebaseAuth.getInstance().getUid(), model.getSenderId())){
            holder.amount.setText(model.getAmount());
           holder.amount.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
        }else{
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        holder.description.setText(model.getDescription());
        holder.time.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VireHolder extends RecyclerView.ViewHolder {
        TextView time,description,amount,day,month;
        public VireHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.currenttime);
            description=itemView.findViewById(R.id.description);
            amount=itemView.findViewById(R.id.tamount);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
            }
    }
}
