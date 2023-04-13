package com.example.Ledgerdiary.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class addsingletimeentry extends AppCompatActivity {
EditText singleamount,singledescription;
ImageView singlecalander,singletimeimg;
CircleImageView singlebackbtn;
TextView singledate,singletime;
String singletext;
Button singlesave;


boolean isclickcalender=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsingletimeentry);

        singleamount=findViewById(R.id.singleamount);
        singledescription=findViewById(R.id.singledescription);
        singlecalander=findViewById(R.id.singlecalander);
        singletimeimg=findViewById(R.id.singletimeimg);
        singledate=findViewById(R.id.singledate);
        singletime=findViewById(R.id.singletime);
        singlesave=findViewById(R.id.singlesave);
        singlebackbtn=findViewById(R.id.singlebackbtn);




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
                        singletext = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                        singledate.setText(singletext);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        singledate.setText(currentDate);

        singlecalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclickcalender=true;
                Date date=Calendar.getInstance().getTime();
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });

        singlebackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);


        singletimeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(addsingletimeentry.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                String selectedTimeString = timeFormat.format(selectedTime.getTime());

                                singletime.setText(selectedTimeString);
                            }
                        }, 10, 0, false);
                timePickerDialog.show();
            }
        });


        singlesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleamount.getText().toString().isEmpty()){
                    singleamount.requestFocus();
                    singleamount.setError("Amount not be empty");
                }else if (singledescription.getText().toString().isEmpty()){
                 singledescription.requestFocus();
                 singledescription.setError("Title not be empty");
                }else if(singledescription.getText().toString().length()>15){
                    singledescription.requestFocus();
                    singledescription.setError("Title size must be less then 16");
                } else if(!isclickcalender){
                    singlecalander.requestFocus();
                }else{
                    Map<String,Object> map=new HashMap<>();
                    map.put("amount",singleamount.getText().toString());
                    map.put("description",singledescription.getText().toString());
                    map.put("date",singledate.getText().toString());
                    map.put("time",singletime.getText().toString());
                    map.put("timestamp",new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("singlereminder")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .push().updateChildren(map);
                         finish();
                }

            }
        });
    }
}