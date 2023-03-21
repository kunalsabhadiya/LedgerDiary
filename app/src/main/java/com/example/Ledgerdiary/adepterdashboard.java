package com.example.Ledgerdiary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class adepterdashboard extends RecyclerView.Adapter<adepterdashboard.ViewHolder> {
    @NonNull
    Activity activity;
    ArrayList list;

    public adepterdashboard(@NonNull Activity activity, ArrayList list) {
        this.activity = activity;
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.finalentry,parent,true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.entryphone.setText(list.get(position).toString());
       holder.entryname.setText(list.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView entryname,entryphone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            entryname =itemView.findViewById(R.id.entryname);
            entryphone = itemView.findViewById(R.id.entryphone);

        }
    }
}
