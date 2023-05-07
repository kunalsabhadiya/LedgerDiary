package com.example.Ledgerdiary.dashboardFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.onlinecustomer.customerInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class onlinercadepter extends RecyclerView.Adapter<onlinercadepter.ViewHolder> {
    Context context;
    ArrayList<onlinemodel> list,filteredList;
    Animation blink;



    public onlinercadepter(Context context, ArrayList<onlinemodel> list) {
        this.context = context;
        this.list = list;
        this.filteredList = list;
    }

    @NonNull
    @Override
    public onlinercadepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finalentry,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onlinemodel model=filteredList.get(position);
        holder.username.setText(model.getCname());
        holder.number.setText(model.getCphonenumber());
        Picasso.get().load(model.getCimageuri()).placeholder(R.drawable.profileimage).into(holder.image);

       blink= AnimationUtils.loadAnimation(context,R.anim.buttonbehaviour);
        int amount=model.getCtamount();
        if(amount<0){
            holder.entryamount.setText(String.valueOf(Math.abs(amount)));
            holder.entryamount.setTextColor(ContextCompat.getColor(context,R.color.green));
        } else if (amount==0) {
            holder.entryamount.setText(String.valueOf(amount));
            holder.entryamount.setTextColor(ContextCompat.getColor(context,R.color.black));
        } else{
            holder.entryamount.setText(String.valueOf(amount));
            holder.entryamount.setTextColor(ContextCompat.getColor(context,R.color.gradcolor1));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lyfinaletnry.startAnimation(blink);
                Intent intent = new Intent(context, customerInterface.class);
                intent.putExtra("ciname",model.getCname());
                intent.putExtra("cnumber",model.getCphonenumber());
                intent.putExtra("oimageuri",model.getCimageuri());
                intent.putExtra("reciveruid",model.getCuid());
                context.startActivity(intent);

            }
        });



    }
    public void filter(String query) {
        filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList = list;
        } else {
            for (onlinemodel item : list) {
                if (item.getCname().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView username,number,entryamount;
        LinearLayout lyfinaletnry;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.entryimage);
            username=itemView.findViewById(R.id.entryname);
            number=itemView.findViewById(R.id.entryphone);
            entryamount=itemView.findViewById(R.id.entryamount);
            lyfinaletnry=itemView.findViewById(R.id.lyfinalentry);


        }
    }
}
