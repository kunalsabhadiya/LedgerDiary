package com.example.Ledgerdiary.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class selectedcustomeradepter extends RecyclerView.Adapter<selectedcustomeradepter.ViewHolder> {
   Context context;
   private ArrayList<groupcustomermodel> list;
   private onSelectedUserClickListner listner;

    public selectedcustomeradepter(Context context, ArrayList<groupcustomermodel> list, onSelectedUserClickListner listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public selectedcustomeradepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.geprofile,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull selectedcustomeradepter.ViewHolder holder, int position) {
       groupcustomermodel groupcustomermodel=list.get(position);
          holder.getext.setText(groupcustomermodel.getCname());
        Picasso.get().load(groupcustomermodel.getCimageuri()).placeholder(R.drawable.profileimage).into(holder.geprofileimage);
        holder.geremoveuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listner.onselecteditemclick(groupcustomermodel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView geprofileimage,geremoveuser;
        TextView getext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            geprofileimage=itemView.findViewById(R.id.geprofileimage);
            geremoveuser=itemView.findViewById(R.id.geremoveuser);
            getext=itemView.findViewById(R.id.getext);
        }
    }
    public interface onSelectedUserClickListner{
        void onselecteditemclick(groupcustomermodel model);
    }
}
