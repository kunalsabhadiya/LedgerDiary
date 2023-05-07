package com.example.Ledgerdiary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.dashboardFragment.onlinemodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class fixentryadapter extends RecyclerView.Adapter<fixentryadapter.ViewHolder> {
    Context context;
    ArrayList<onlinemodel> list;
    String sdate,edate;

    public fixentryadapter(Context context, ArrayList<onlinemodel> list,String sdate,String edate) {
        this.context = context;
        this.list = list;
        this.sdate = sdate;
        this.edate = edate;
    }

    @NonNull
    @Override
    public fixentryadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.finalentry,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fixentryadapter.ViewHolder holder, int position) {
       onlinemodel onlinemodel=list.get(position);
       holder.number.setText(onlinemodel.getCphonenumber());
        holder.username.setText(onlinemodel.getCname());
        Picasso.get().load(onlinemodel.getCimageuri()).placeholder(R.drawable.profileimage).into(holder.image);

        if(onlinemodel.getCtamount() >0){
            holder.entryamount.setText(String.valueOf(onlinemodel.getCtamount()));
            holder.entryamount.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
        }else{
            holder.entryamount.setText(String.valueOf(Math.abs(onlinemodel.getCtamount())));
            holder.entryamount.setTextColor(ContextCompat.getColor(context,R.color.green));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,userreporttransaction.class);
                intent.putExtra("uid",onlinemodel.getCuid());
                intent.putExtra("tamount",String.valueOf(onlinemodel.getCtamount()));
                intent.putExtra("name",onlinemodel.getCname());
                intent.putExtra("sdate",sdate);
                intent.putExtra("edate",edate);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView username,number,entryamount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                image=itemView.findViewById(R.id.entryimage);
                username=itemView.findViewById(R.id.entryname);
                number=itemView.findViewById(R.id.entryphone);
                entryamount=itemView.findViewById(R.id.entryamount);

            }
    }
}
