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

import java.util.ArrayList;

public class pdfadapter extends RecyclerView.Adapter<pdfadapter.ViewHolder> {
    Context context;
    ArrayList<pdfmodel> list;

    public pdfadapter(Context context, ArrayList<pdfmodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public pdfadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.pdfrvview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pdfadapter.ViewHolder holder, int position) {
        pdfmodel pdfmodel=list.get(position);
        holder.pdfdate.setText(pdfmodel.getDate());
        holder.pdfdescription.setText(pdfmodel.getDescription());
        holder.pdftotal.setText(String.valueOf(pdfmodel.getTotal()));
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(pdfmodel.getSenderId())){
            holder.pdfsend.setText(pdfmodel.getAmount());
            holder.pdfreceive.setText("-");
        }else{
            holder.pdfsend.setText("-");
            holder.pdfreceive.setText(pdfmodel.getAmount());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public pdfmodel getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pdfdate,pdfdescription,pdftotal,pdfsend,pdfreceive;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfdate=itemView.findViewById(R.id.pdfdate);
            pdfdescription=itemView.findViewById(R.id.pdfdescription);
            pdftotal=itemView.findViewById(R.id.pdftotal);
            pdfsend=itemView.findViewById(R.id.pdfsend);
            pdfreceive=itemView.findViewById(R.id.pdfreceive);

        }
    }
}
