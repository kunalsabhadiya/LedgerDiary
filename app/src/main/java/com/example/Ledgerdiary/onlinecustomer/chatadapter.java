package com.example.Ledgerdiary.onlinecustomer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;



public class chatadapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<chatmodel> chatlist;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public chatadapter(Context context, ArrayList<chatmodel> chatlist) {
        this.context = context;
        this.chatlist = chatlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.senderchat, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciverchat, parent, false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatmodel model = chatlist.get(position);
        Date date = new Date(model.getTimestamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = dateFormat.format(date);


        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder ViewHolder = (senderViewHolder) holder;
            ViewHolder.message.setText(model.getMsg());
            ViewHolder.time.setText(formattedTime);
            Picasso.get().load(model.getImg()).placeholder(R.drawable.profileimage).into(ViewHolder.profile);
        } else {
            reciverViewHolder ViewHolder = (reciverViewHolder) holder;
            ViewHolder.message.setText(model.getMsg());
            ViewHolder.time.setText(formattedTime);
            Picasso.get().load(model.getImg()).placeholder(R.drawable.profileimage).into(ViewHolder.profile);

        }


    }


    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    @Override
    public int getItemViewType(int position) {

        chatmodel model = chatlist.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getChatsenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        TextView message,time;
        CircleImageView profile;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.sendertime);
           message=itemView.findViewById(R.id.sendermessage);
           profile=itemView.findViewById(R.id.senderprofile);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        TextView message,time;
        CircleImageView profile;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id .receivertime);
            message=itemView.findViewById(R.id.recivermessage);
            profile=itemView.findViewById(R.id.receiverprofile);
        }
    }

}
