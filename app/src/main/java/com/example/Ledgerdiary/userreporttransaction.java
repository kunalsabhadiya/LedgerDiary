package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.onlinecustomer.transactionmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class userreporttransaction extends AppCompatActivity {
String uid,name,amount,sdate,edate;
RecyclerView pdfrc;
SimpleDateFormat sdfinput,sdfoutput;
Date startdt,enddt,formatedDate;

ArrayList<transactionmodel> list;
userreporttransactionadepter adepter;
TextView selectedname,selectedsdate,selectededate,selectedamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userreporttransaction);
    uid=getIntent().getStringExtra("uid");
    name=getIntent().getStringExtra("name");
     amount=getIntent().getStringExtra("tamount");
     sdate=getIntent().getStringExtra("sdate");
     edate=getIntent().getStringExtra("edate");

     list=new ArrayList<>();
     adepter=new userreporttransactionadepter(list,this);

        pdfrc=findViewById(R.id.pdfrc);
        selectedname=findViewById(R.id.selectedpdfname);
        selectedsdate=findViewById(R.id.selectedstartdate);
        selectededate=findViewById(R.id.selectedenddate);
        selectedamount=findViewById(R.id.selectedamount);

        selectedname.setText(name);
        selectedsdate.setText(sdate);
        selectededate.setText(edate);
        selectedamount.setText(Math.abs(Integer.parseInt(amount)));
        if(Integer.parseInt(amount)>0){
            selectedamount.setTextColor(ContextCompat.getColor(this,R.color.gradcolor1));
        }else if(Integer.parseInt(amount)<0){
            selectedamount.setTextColor(ContextCompat.getColor(this,R.color.green));
        }else{
            selectedamount.setTextColor(ContextCompat.getColor(this,R.color.black));
        }


        sdfinput = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        sdfoutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            startdt=sdfoutput.parse(sdfoutput.format(Objects.requireNonNull(sdfoutput.parse(sdate))));
            enddt=sdfoutput.parse(sdfoutput.format(Objects.requireNonNull(sdfoutput.parse(edate))));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid()+uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        list.clear();
                         if (snapshot2.exists()) {
                            for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                String date = snapshot3.child("date").getValue(String.class);
                                try {
                                    assert date != null;
                                    formatedDate = sdfoutput.parse(sdfoutput.format(Objects.requireNonNull(sdfinput.parse(date))));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                assert formatedDate != null;
                                if ((formatedDate.equals(startdt) || formatedDate.after(startdt)) && (formatedDate.equals(enddt) || formatedDate.before(enddt))) {
                                   transactionmodel model=snapshot3.getValue(transactionmodel.class);
                                     list.add(model);
                                }

                            }
                             adepter.notifyDataSetChanged();

                        }


                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        pdfrc.setLayoutManager(new LinearLayoutManager(this));
        pdfrc.setAdapter(adepter);







    }
}