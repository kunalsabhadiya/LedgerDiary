package com.example.Ledgerdiary.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class updatesingletimeentry extends AppCompatActivity {
EditText upsingleamount,upsingledescription;

ImageView upsinglecalander,upsingletimeimg;

CircleImageView upsinglebackbtn,upsinglesubmitbtn;

TextView upsingledate,upsingletime,updatedue;

String upamount,updescription,uptime,update,updue,timestamp;

RelativeLayout upsingledeletebtn,upsinglepaidbtn;
LottieAnimationView animpaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatesingletimeentry);
        upsingleamount=findViewById(R.id.upsingleamount);
        upsingledescription=findViewById(R.id.upsingledescription);
        upsinglecalander=findViewById(R.id.upsinglecalander);
        upsingletimeimg=findViewById(R.id.upsingletimeimg);
        upsinglebackbtn=findViewById(R.id.upsinglebackbtn);
        upsinglesubmitbtn=findViewById(R.id.upsinglesubmitbtn);
        upsingledate=findViewById(R.id.upsingledate);
        upsingletime=findViewById(R.id.upsingletime);
        upsingledeletebtn=findViewById(R.id.upsingledeletebtn);
        upsinglepaidbtn=findViewById(R.id.upsinglepaidbtn);
        animpaid=findViewById(R.id.animpaid);

        updatedue=findViewById(R.id.updatedue);

        upamount=getIntent().getStringExtra("occamount");
        updescription=getIntent().getStringExtra("occdescription");
        uptime=getIntent().getStringExtra("occtime");
        update=getIntent().getStringExtra("occdate");
        updue=getIntent().getStringExtra("occdue");
        timestamp=getIntent().getStringExtra("timestamp");

        int due=Integer.parseInt(updue);
        if(due==0){
            updatedue.setText( "Due today");
            updatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }else if(due>0){
            updatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            updatedue.setText( "Due in "+ due +" days");
        }else{
            updatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
            updatedue.setText( "Due was "+ update);
        }

        upsingleamount.setText(upamount);
        upsingledescription.setText(updescription);
        upsingledate.setText(update);
        upsingletime.setText(uptime);



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
                        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
                        String singletext = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                        upsingledate.setText(singletext);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );


        upsinglecalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date=Calendar.getInstance().getTime();
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });

        upsinglebackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        upsingletimeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(updatesingletimeentry.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                String selectedTimeString = timeFormat.format(selectedTime.getTime());

                                upsingletime.setText(selectedTimeString);
                            }
                        }, 10, 0, false);
                timePickerDialog.show();
            }
        });


        upsinglepaidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(updatesingletimeentry.this);
                builder.setTitle("Paid ?");
                builder.setMessage("Confirmation of paid.");
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("singlereminder")
                                .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                                .equalTo(Long.parseLong(timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                                if(snapshot1.exists() && snapshot1.child("timestamp").getValue().equals(Long.parseLong(timestamp))){
                                                    snapshot1.getRef().removeValue();
                                                    animpaid.setVisibility(View.VISIBLE);
                                                    animpaid.playAnimation();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            animpaid.cancelAnimation();
                                                            finish();
                                                            animpaid.setVisibility(View.INVISIBLE);
                                                        }
                                                    }, 2000);
                                                }

                                            }

                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });
                builder.setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
        });
        upsinglesubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upsingleamount.getText().toString().isEmpty()){
                    upsingleamount.requestFocus();
                    upsingleamount.setError("Amount not be empty");
                }else if (upsingledescription.getText().toString().isEmpty()){
                    upsingledescription.requestFocus();
                    upsingledescription.setError("Title not be empty");
                }else if(upsingledescription.getText().toString().length()>15){
                    upsingledescription.requestFocus();
                    upsingledescription.setError("Title size must be less then 16");
                } else{
                    FirebaseDatabase.getInstance().getReference().child("singlereminder")
                            .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                            .equalTo(Long.parseLong(timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            String snap=snapshot1.getKey();

                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                                            LocalDate futureDate = LocalDate.parse(upsingledate.getText().toString(), formatter);
                                            LocalDate today = LocalDate.now();
                                            long daysBetween = ChronoUnit.DAYS.between(today, futureDate);

                                            Map<String,Object> map=new HashMap<>();
                                            map.put("amount",upsingleamount.getText().toString());
                                            map.put("description",upsingledescription.getText().toString());
                                            map.put("date",upsingledate.getText().toString());
                                            map.put("time",upsingletime.getText().toString());
                                            FirebaseDatabase.getInstance().getReference().child("singlereminder")
                                                    .child(FirebaseAuth.getInstance().getUid()).child(snap).updateChildren(map);
                                            Toast.makeText(updatesingletimeentry.this, "Reminder updated sucessfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });
        upsingledeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(updatesingletimeentry.this);
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete reminder?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("singlereminder")
                                .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                                .equalTo(Long.parseLong(timestamp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                           for(DataSnapshot snapshot1:snapshot.getChildren()){
                                               if(snapshot1.exists() && snapshot1.child("timestamp").getValue().equals(Long.parseLong(timestamp))){
                                                   Log.e("valuee",String.valueOf(snapshot1.getKey()));
                                                   snapshot1.getRef().removeValue();
                                                   finish();
                                               }
                                           }
                                            }
                                        }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
        });
    }
}