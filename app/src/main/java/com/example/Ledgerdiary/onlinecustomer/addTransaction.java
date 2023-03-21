package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class addTransaction extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
CircleImageView addbackbtn,addciprofile;
EditText addamount,adddescription;
TextView addciname,adddatetv;
Button addsave;
ImageView calander,addcallbtn;
    String text;
String senderroom,reciverroom,usernumber,username;
RelativeLayout addrlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        addbackbtn=findViewById(R.id.addbackbtn);
        addcallbtn=findViewById(R.id.addcallbtn);
        addciprofile=findViewById(R.id.addciprofile);

        addamount=findViewById(R.id.addamount);
        adddescription=findViewById(R.id.adddescription);

        addciname=findViewById(R.id.addciname);
        adddatetv=findViewById(R.id.adddate);

        addsave=findViewById(R.id.addsave);

        calander=findViewById(R.id.calander);

        addrlayout=findViewById(R.id.addrlayout);

        String name=getIntent().getStringExtra("ciname");
        String number=getIntent().getStringExtra("mnumber");
        String imguri=getIntent().getStringExtra("ciprofile");
        String reciveruid = getIntent().getStringExtra("ruid");
        String senderuid = FirebaseAuth.getInstance().getUid();

        senderroom=senderuid+reciveruid;
        reciverroom=reciveruid+senderuid;

        usernumber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().replace("+","");
/*
        addamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addciprofile.setVisibility(View.INVISIBLE);
                addciname.setVisibility(View.INVISIBLE);
                addcallbtn.setVisibility(View.INVISIBLE);
                addbackbtn.setVisibility(View.INVISIBLE);
                addrlayout.startAnimation();

            }
        });


 */
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(senderuid).child("username")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                username = snapshot.getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        addciname.setText(name);
        Picasso.get().load(imguri).placeholder(R.drawable.profileimage).into(addciprofile);

        addcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });


        addbackbtn.setOnClickListener(new View.OnClickListener() {
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
                        adddatetv.setText(text);
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
        adddatetv.setText(defaultDate);
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });



        addsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addamount.getText().toString().isEmpty()){
                    addamount.setError("Amount is required");
                    addamount.requestFocus();
                }else{
                    String desc=adddescription.getText().toString();
                    if(desc.isEmpty()){
                        desc="Transaction";
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);

                    transactionmodel model=new transactionmodel(senderuid,desc, addamount.getText().toString(), date.getTime(),adddatetv.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("transactions")
                            .child(senderroom)
                            .push().setValue(model)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference().child("transactions")
                                            .child(reciverroom)
                                            .push().setValue(model)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(addTransaction.this, "Data added sucessfully", Toast.LENGTH_SHORT).show();
                                                    adddescription.setText("");
                                                    addamount.setText("");
                                                     FirebaseDatabase.getInstance().getReference().child("users")
                                                            .child(reciveruid).child("customer").orderByChild("Cphonenumber").equalTo(usernumber)
                                                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                     if(!snapshot.exists()){
                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                 .child(reciveruid).child("customer")
                                                                                 .child(usernumber).child("Cphonenumber").setValue(usernumber);
                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                 .child(reciveruid).child("customer")
                                                                                 .child(usernumber).child("Cname").setValue(username);
                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                 .child(reciveruid).child("customer")
                                                                                 .child(usernumber).child("Cuid").setValue(senderuid);
                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                 .child(reciveruid).child("customer")
                                                                                 .child(usernumber).child("Ctamount").setValue("0");
                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                 .child(senderuid).child("imageUri").addValueEventListener(new ValueEventListener() {
                                                                                     @Override
                                                                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                         FirebaseDatabase.getInstance().getReference().child("users")
                                                                                                 .child(reciveruid).child("customer")
                                                                                                 .child(usernumber).child("Cimageuri").setValue(snapshot.getValue().toString());
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



                                                    finish();
                                                }
                                            });
                                }
                            });
                }
            }
        });

    }





    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );


        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        adddatetv.setText(selectedDate);
    }


}