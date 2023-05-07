package com.example.Ledgerdiary.dashboardFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.offlinecustomer.citable;
import com.example.Ledgerdiary.offlinecustomer.ocustomerinterface;

import java.util.ArrayList;
import java.util.List;

public class offlineadepter extends RecyclerView.Adapter<offlineadepter.Viewholder> {

    private Context context;

    private List<citable>  list,filterdlist;

    public offlineadepter(Context context, List<citable> list) {
        this.context = context;
        this.list = list;
        this.filterdlist=list;
    }

    @NonNull
    @Override
    public offlineadepter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.finalentry,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull offlineadepter.Viewholder holder, int position) {
       citable citable=filterdlist.get(position);
        holder.name.setText(String.valueOf(citable.getCiname()));
        holder.number.setText(String.valueOf(citable.getCiphonenumber()));
        if(citable.getCitamount()<0){
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.amount.setText(String.valueOf(Math.abs(citable.getCitamount())));
        } else if (citable.getCitamount()==0) {
            holder.amount.setText(String.valueOf(citable.getCitamount()));
        }else{
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
            holder.amount.setText(String.valueOf(citable.getCitamount()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ocustomerinterface.class);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ocustomerinterface.class);
                intent.putExtra("phonenumber",holder.number.getText().toString());
                intent.putExtra("name",holder.name.getText().toString());
                intent.putExtra("index",String.valueOf(citable.getCiuid()));
                context.startActivity(intent);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete from customer?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return true;
            }
        });

    }
    public void filter(String query) {
        filterdlist = new ArrayList<>();
        if (query.isEmpty()) {
            filterdlist = list;
        } else {
            for (citable item : list) {
                if (item.getCiname().toLowerCase().contains(query.toLowerCase())) {
                    filterdlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return filterdlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
       TextView name,number,amount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.entryname);
            number=itemView.findViewById(R.id.entryphone);
            amount=itemView.findViewById(R.id.entryamount);
        }
    }

}
