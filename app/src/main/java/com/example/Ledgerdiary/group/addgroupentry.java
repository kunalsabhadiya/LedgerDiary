package com.example.Ledgerdiary.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.Dashboard;
import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.dashboardFragment.onlinemodel;
import com.example.Ledgerdiary.onlinecustomer.transactionmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class addgroupentry extends AppCompatActivity {
RecyclerView geselectedrc,gecustomerlistrc;
ImageView gecalender;
CircleImageView gebackbtn,gesubmit;
EditText geamount,gedescription;
TextView gedate;
String text;
groupcustomeradepter adepter;
selectedcustomeradepter selectedadepter;
ArrayList<groupcustomermodel> list,selectedlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroupentry);

        geselectedrc=findViewById(R.id.geselectedrc);
        gecustomerlistrc=findViewById(R.id.gecustomerlistrc);

        gecalender=findViewById(R.id.gecalander);

        gebackbtn=findViewById(R.id.gebackbtn);
        gesubmit=findViewById(R.id.gesubmit);

        geamount=findViewById(R.id.geamount);
        gedescription=findViewById(R.id.gedescription);

        gedate=findViewById(R.id.gedate);



        list=new ArrayList<>();
        selectedlist=new ArrayList<>();
        adepter=new groupcustomeradepter(getApplicationContext(), list, new groupcustomeradepter.ongroupItemClickListner() {
            @Override
            public void ongroupitemclicklistner(groupcustomermodel model) {
                selectedlist.add(model);
                list.remove(model);
                selectedadepter.notifyDataSetChanged();
                adepter.notifyDataSetChanged();
            }
        });
        selectedadepter=new selectedcustomeradepter(getApplicationContext(), selectedlist, new selectedcustomeradepter.onSelectedUserClickListner() {
            @Override
            public void onselecteditemclick(groupcustomermodel model) {
                selectedlist.remove(model);
                list.add(model);
                selectedadepter.notifyDataSetChanged();
                adepter.notifyDataSetChanged();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("customer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            groupcustomermodel model=snapshot1.getValue(groupcustomermodel.class);
                            list.add(model);
                        }
                        adepter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        gecustomerlistrc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        gecustomerlistrc.setAdapter(adepter);
        geselectedrc.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        geselectedrc.setAdapter(selectedadepter);


        gebackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Calendar mcalendar = Calendar.getInstance();
        int initialYear = mcalendar.get(Calendar.YEAR);
        int initialMonth = mcalendar.get(Calendar.MONTH);
        int initialDay = mcalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = null;
                        try {
                            date = sdf.parse(String.format("%02d/%02d/%04d", dayOfMonth, month+1, year));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                        mcalendar.setTime(date);
                        int dayNumber = mcalendar.get(Calendar.DAY_OF_MONTH);
                        int monthNumber = mcalendar.get(Calendar.MONTH);

                        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
                        String[] months = dfs.getMonths();
                        String monthName = months[monthNumber];

                        text = String.format("%02d %s", dayNumber, monthName);
                        gedate.setText(text);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
        int mdayNumber = mcalendar.get(Calendar.DAY_OF_MONTH);
        int mmonthNumber = mcalendar.get(Calendar.MONTH);
        DateFormatSymbols mdfs = new DateFormatSymbols(Locale.getDefault());
        String[] mmonths = mdfs.getMonths();
        String mmonthName = mmonths[mmonthNumber];
        String defaultDate = String.format("%02d %s", mdayNumber, mmonthName);
        gedate.setText(defaultDate);

        gecalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });




        gesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(geamount.getText().toString().isEmpty()){
                    geamount.setError("amount is required");
                    geamount.requestFocus();
                }else if(gedescription.getText().toString().isEmpty()){
                    gedescription.setError("please provide description to customer");
                }else if(selectedlist.size()==0){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please select customer", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(getResources().getColor(R.color.gradcolor1));
                    snackbar.setTextColor(getResources().getColor(R.color.black));
                    snackbar.show();
                }else{
                    double addamount=Double.parseDouble(geamount.getText().toString())/(selectedlist.size()+1);
                    String famount=String.valueOf(Math.round(addamount));
                    String description=gedescription.getText().toString();
                    String date=gedate.getText().toString();
                    String senderid=FirebaseAuth.getInstance().getUid();
                    Date d = new Date(System.currentTimeMillis());
                    long timestamp=d.getTime();
                    for(int i=0;i<selectedlist.size();i++){
                        transactionmodel transactionmodel=new transactionmodel(senderid,description,famount,timestamp,date);
                        int finalI = i;
                        FirebaseDatabase.getInstance().getReference().child("transactions")
                                .child(senderid+selectedlist.get(i).getCuid()).push().setValue(transactionmodel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("transactions")
                                                .child(selectedlist.get(finalI).getCuid()+senderid).push().setValue(transactionmodel)
                                                .addOnCompleteListener(task1 -> {
                                                    Toast.makeText(getApplicationContext(),"Group entry added sucessfully",Toast.LENGTH_SHORT);
                                                    geamount.setText("");
                                                    gedescription.setText("");
                                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                                });
                                    }
                                });
                    }




                }
            }
        });


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


}