package com.example.Ledgerdiary.group;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.dashboardFragment.onlinemodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class groupcustomeradepter extends RecyclerView.Adapter<groupcustomeradepter.ViewHolder> {
    Context context;
    ongroupItemClickListner ongroupItemClicklistner;
    ArrayList<groupcustomermodel> list,filterdList;

    public groupcustomeradepter(Context context, ArrayList<groupcustomermodel> list,ongroupItemClickListner ongroupItemClicklistner) {
        this.context = context;
        this.list = list;
        this.filterdList = list;
        this.ongroupItemClicklistner = ongroupItemClicklistner;
    }

    @NonNull
    @Override
    public groupcustomeradepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.gecustomerlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull groupcustomeradepter.ViewHolder holder, int position) {
      groupcustomermodel model= filterdList.get(position);
      holder.customername.setText(model.getCname());
      holder.customernumber.setText(model.getCphonenumber());
        Picasso.get().load(model.getCimageuri()).placeholder(R.drawable.profileimage).into(holder.customerimage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongroupItemClicklistner.ongroupitemclicklistner(model);
            }
        });
    }
    public void filter(String query) {
        filterdList = new ArrayList<>();
        if (query.isEmpty()) {
            filterdList = list;
        } else {
            for (groupcustomermodel item : list) {
                if (item.getCname().toLowerCase().contains(query.toLowerCase())) {
                    filterdList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filterdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView customerimage;
        TextView customername,customernumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerimage=itemView.findViewById(R.id.gecustomerimage);
            customername=itemView.findViewById(R.id.gecustomername);
            customernumber=itemView.findViewById(R.id.gephone);

        }
    }
    public interface ongroupItemClickListner{
        void ongroupitemclicklistner(groupcustomermodel model);
    }
}
