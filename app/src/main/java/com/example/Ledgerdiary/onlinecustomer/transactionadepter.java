package com.example.Ledgerdiary.onlinecustomer;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class transactionadepter extends RecyclerView.Adapter {
    Context context;
    ArrayList<transactionmodel> transactionlist;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;
    int totalamount=0;
    TextView citotal;
    String reciveruid;
    OnAdepterInterraction onAdepterInterraction;

    public void setOnAdepterInterraction(OnAdepterInterraction onAdepterInterraction) {
        this.onAdepterInterraction = onAdepterInterraction;
    }

    public transactionadepter(Context context, ArrayList<transactionmodel> transactionlist, TextView citotal, String reciveruid) {
        this.context = context;
        this.transactionlist = transactionlist;
        this.citotal = citotal;
        this.reciveruid = reciveruid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==ITEM_SEND){
          View view= LayoutInflater.from(context).inflate(R.layout.transactionentry,parent,false);
          return new senderViewHolder(view);
       }else{
           View view= LayoutInflater.from(context).inflate(R.layout.transactionreceiverentry,parent,false);
           return new reciverViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        transactionmodel model=transactionlist.get(position);
        String datee=model.getDate();
        String[] parts = datee.split(" ");
        String day = parts[0];
        String month= parts[1];
        Date date=new Date(model.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = dateFormat.format(date);


        if(holder.getClass() == senderViewHolder.class){
            senderViewHolder ViewHolder=(senderViewHolder) holder;
            ViewHolder.day.setText(day);
            ViewHolder.month.setText(month);
            ViewHolder.amount.setText(model.getAmount());
            ViewHolder.description.setText(model.getDescription());
            ViewHolder.time.setText(formattedTime);
            totalamount+=Integer.parseInt(model.getAmount());


            FirebaseDatabase.getInstance().getReference()
                            .child("transactions").child(FirebaseAuth.getInstance().getUid()+reciveruid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String messageId = childSnapshot.getKey();
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent=new Intent(context,updateTransaction.class);
                                                intent.putExtra("timestamp", String.valueOf(model.getTimestamp()));
                                                intent.putExtra("senderroom", FirebaseAuth.getInstance().getUid()+reciveruid);
                                                intent.putExtra("msgid",messageId);
                                                intent.putExtra("amount",model.getAmount());
                                                intent.putExtra("description",model.getDescription());
                                                intent.putExtra("datee",datee);
                                                context.startActivity(intent);
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


        }else{
            reciverViewHolder ViewHolder=(reciverViewHolder) holder;
            ViewHolder.day.setText(day);
            ViewHolder.month.setText(month);
            ViewHolder.amount.setText(model.getAmount());
            ViewHolder.description.setText(model.getDescription());
            ViewHolder.time.setText(formattedTime);
            totalamount-=Integer.parseInt(model.getAmount());
        }


    }


    @Override
    public int getItemCount() {
        return transactionlist.size();
    }

    @Override
    public int getItemViewType(int position) {

        transactionmodel model=transactionlist.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getSenderId())){
            return ITEM_SEND;
        }else{
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        TextView time,description,amount,day,month;


        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdepterInterraction.onTouchListner(getAdapterPosition());
                }
            });
            time=itemView.findViewById(R.id.currenttime);
            description=itemView.findViewById(R.id.description);
            amount=itemView.findViewById(R.id.tamount);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder{
        TextView time,description,amount,day,month;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.currenttime);
            description=itemView.findViewById(R.id.description);
            amount=itemView.findViewById(R.id.tamount);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
        }
    }
    public interface OnAdepterInterraction{
        void onTouchListner(int position);
    }

}
