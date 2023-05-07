package com.example.Ledgerdiary.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class addoccurance extends AppCompatActivity {
ImageView occtimeimg,occcalender;
TextView occtime,occdate,occduedate,occdateshow,occperiodshow;
Spinner occuringtime;
EditText occamount,occdescription;
CircleImageView occsubmitbtn,occbackbtn;
String spinnerselecteditem=null;

boolean isclickspinner=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addoccurance);

        occtimeimg=findViewById(R.id.occtimeimg);
        occtime=findViewById(R.id.occtime);
        occcalender=findViewById(R.id.occcalander);
        occdate=findViewById(R.id.occdate);
        occamount=findViewById(R.id.occamount);
        occsubmitbtn=findViewById(R.id.occsubmitbtn);
        occdescription=findViewById(R.id.occdescription);
        occuringtime=findViewById(R.id.occuringtime);
        occbackbtn=findViewById(R.id.occbackbtn);
        occduedate=findViewById(R.id.occduedate);
        occdateshow=findViewById(R.id.occdateshow);
        occperiodshow=findViewById(R.id.occperiodshow);

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

                        String singletext = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
                        occdate.setText(singletext);
                        occdateshow.setText("Your reminder start at "+singletext);

                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );

        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        occdate.setText(currentDate);
        occcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclickspinner=true;
                Date date=Calendar.getInstance().getTime();
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });

        ArrayList<String> arrlist=new ArrayList<>();
        arrlist.add("Daily");
        arrlist.add("Weekly");
        arrlist.add("Every 15 days");
        arrlist.add("Monthly");
        arrlist.add("Every 3 months");
        arrlist.add("Quarterly");
        arrlist.add("Annually");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(addoccurance.this, android.R.layout.simple_list_item_1,arrlist);
        occuringtime.setAdapter(arrayAdapter);


        occuringtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerselecteditem=arrlist.get(i);
                if(isclickspinner){
                    occduedate.setVisibility(View.VISIBLE);
                }
                occperiodshow.setText("Occurance time is "+spinnerselecteditem);
                try {
                    occduedate.setText("Next due is "+spinnerdate(spinnerselecteditem));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        occtimeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(addoccurance.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                String selectedTimeString = timeFormat.format(selectedTime.getTime());
                                occtime.setText(selectedTimeString);
                            }
                        }, 10, 0, false);
                timePickerDialog.show();
            }
        });

        occsubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(occamount.getText().toString().isEmpty()){
                    occamount.requestFocus();
                    occamount.setError("Amount not be empty");
                }else if (occdescription.getText().toString().isEmpty()){
                    occdescription.requestFocus();
                    occdescription.setError("Title not be empty");
                }else if(occdescription.getText().toString().length()>15){
                    occdescription.requestFocus();
                    occdescription.setError("Title size must be less then 16");
                }else{


                    Map<String,Object> map=new HashMap<>();
                    map.put("amount",occamount.getText().toString());
                    map.put("description",occdescription.getText().toString());
                    map.put("date",occdate.getText().toString());
                    map.put("time",occtime.getText().toString());
                    map.put("timestamp",new Date().getTime());
                    map.put("occtime",spinnerselecteditem);
                    FirebaseDatabase.getInstance().getReference().child("occurance")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .push().updateChildren(map);

                    occdateshow.setText("");
                    occduedate.setText("");
                    occperiodshow.setText("");
                    isclickspinner=false;
                    finish();
                }
            }
        });

     occbackbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             finish();
         }
     });




    }
    private String spinnerdate(String spinnerselecteditem) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date userInputDate = sdf.parse(occdate.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(userInputDate);
        switch (spinnerselecteditem) {
            case "Daily":
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case "Every 15 days":
                calendar.add(Calendar.DAY_OF_YEAR, 15);
                break;
            case "Weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "Monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "Every 3 months":
                calendar.add(Calendar.MONTH, 3);
                break;
            case "Quarterly":
                calendar.add(Calendar.MONTH, 3);
                break;
            case "Yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
        }

        return sdf.format(calendar.getTime());
    }
}