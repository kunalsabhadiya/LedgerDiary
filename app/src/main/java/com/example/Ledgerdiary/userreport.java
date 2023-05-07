package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.dashboardFragment.onlinemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class userreport extends AppCompatActivity {
TextView tvstartdate,tvenddate,ttotal;
RecyclerView rvtransactionacc;
ImageView backbtn;
String startdate,enddate;
ArrayList<onlinemodel> list;
int totalll=0;
    Date formatedDate,startdt,enddt;
fixentryadapter fixentryadapter;
ProgressBar pg;
SimpleDateFormat sdfinput,sdfoutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userreport);
        startdate = getIntent().getStringExtra("startdate");
        enddate = getIntent().getStringExtra("enddate");

        tvstartdate=findViewById(R.id.tvstartdate);
        tvenddate=findViewById(R.id.tvenddate);
        ttotal=findViewById(R.id.ttotal);
        rvtransactionacc=findViewById(R.id.rvtransactionacc);
        backbtn=findViewById(R.id.backbtn);
        pg=findViewById(R.id.pguserreport);


        tvstartdate.setText(startdate);
        tvenddate.setText(enddate);
        list=new ArrayList<>();
        fixentryadapter=new fixentryadapter(this,list,startdate,enddate);

        sdfinput = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        sdfoutput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            startdt=sdfoutput.parse(sdfoutput.format(Objects.requireNonNull(sdfoutput.parse(startdate))));
            enddt=sdfoutput.parse(sdfoutput.format(Objects.requireNonNull(sdfoutput.parse(enddate))));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        pg.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .child("customer").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            onlinemodel model=snapshot1.getValue(onlinemodel.class);
                            assert model != null;
                            FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid()+model.getCuid())
                                  .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                             int amount = 0;
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
                                                             if (Objects.equals(snapshot3.child("senderId").getValue(String.class), FirebaseAuth.getInstance().getUid())) {
                                                                amount += Integer.parseInt(Objects.requireNonNull(snapshot3.child("amount").getValue(String.class)));
                                                            } else {
                                                                amount -= Integer.parseInt(Objects.requireNonNull(snapshot3.child("amount").getValue(String.class)));
                                                            }
                                                        }

                                                    }

                                                if(amount!=0){
                                                    model.setCtamount(amount);
                                                    list.add(model);
                                                    totalll+=amount;
                                                }
                                                fixentryadapter.notifyDataSetChanged();
                                                pg.setVisibility(View.INVISIBLE);
                                                }
                                            if(totalll>0){
                                                ttotal.setText(String.valueOf(totalll));
                                                ttotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
                                            }else{
                                                ttotal.setText(String.valueOf(Math.abs(totalll)));
                                                ttotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                                            }
                                            }
                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {

                                      }
                                  });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


        rvtransactionacc.setLayoutManager(new LinearLayoutManager(this));
        rvtransactionacc.setAdapter(fixentryadapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      finish();
    }
}