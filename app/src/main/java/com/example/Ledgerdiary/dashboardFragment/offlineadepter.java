package com.example.Ledgerdiary.dashboardFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.sqliteDbhelper;

import java.util.ArrayList;

public class offlineadepter extends RecyclerView.Adapter<offlineadepter.Viewholder> {

    private Context context;

    private ArrayList  c_name,c_number;
    String id;


    public offlineadepter(Context context, ArrayList c_name, ArrayList c_number) {
        this.context = context;
        this.c_name = c_name;
        this.c_number = c_number;

    }

    @NonNull
    @Override
    public offlineadepter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.finalentry,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull offlineadepter.Viewholder holder, int position) {
        holder.name.setText(String.valueOf(c_name.get(position)));
        holder.number.setText(String.valueOf(c_number.get(position)));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            sqliteDbhelper db=new sqliteDbhelper(context);
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete from customer?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqliteDbhelper myDB = new sqliteDbhelper(context);
                        myDB.deleteentry(holder.getAdapterPosition()+1);
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return c_name.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
       TextView name,number;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.entryname);
            number=itemView.findViewById(R.id.entryphone);
        }
    }

}
