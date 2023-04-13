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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateoccurance extends AppCompatActivity {
CircleImageView upoccbackbtn,upoccsubmitbtn;
ImageView upocccalander,upocctimeimg;
TextView upoccperiodshow,upoccdateshow,upoccduedate,upocctime,upoccdate,upoccdatedue;
EditText upoccamount,upoccdesc;
Spinner upoccuringtime ;
RelativeLayout upoccdeletebtn,upoccpaidbtn;
LottieAnimationView upanimpaid;
String spinnerselecteditem=null;

boolean isclickspinner=false;

String upamountint,updescriptionint,uptimestampint,uptimeint,updateint,updueint,upocctypeint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateoccurance);

        upoccbackbtn=findViewById(R.id.upoccbackbtn);
        upoccsubmitbtn=findViewById(R.id.upoccsubmitbtn);
        upocctimeimg=findViewById(R.id.upocctimeimg);
        upocccalander=findViewById(R.id.upocccalander);
        upoccperiodshow=findViewById(R.id.upoccperiodshow);
        upoccdateshow=findViewById(R.id.upoccdateshow);
        upoccduedate=findViewById(R.id.upoccduedate);
        upocctime=findViewById(R.id.upocctime);
        upoccdate=findViewById(R.id.upoccdate);
        upoccamount=findViewById(R.id.upoccamount);
        upoccdesc=findViewById(R.id.upoccdescription);
        upoccuringtime=findViewById(R.id.upoccuringtime);
        upoccdeletebtn=findViewById(R.id.upoccdeletebtn);
        upoccpaidbtn=findViewById(R.id.upoccpaidbtn);
        upanimpaid=findViewById(R.id.upanimpaid);
        upoccdatedue=findViewById(R.id.upoccdatedue);

        upamountint=getIntent().getStringExtra("occamount");
        updescriptionint=getIntent().getStringExtra("occdescription");
        uptimestampint=getIntent().getStringExtra("timestamp");
        uptimeint=getIntent().getStringExtra("occtime");
        updateint=getIntent().getStringExtra("occdate");
        updueint=getIntent().getStringExtra("occdue");
        upocctypeint=getIntent().getStringExtra("reccurancetype");


        int due=Integer.parseInt(updueint);
        if(due==0){
            upoccdatedue.setText( "Due today");
            upoccdatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }else if(due>0){
            upoccdatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            upoccdatedue.setText( "Due in "+ updueint +" days");
        }else{
            upoccdatedue.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
            upoccdatedue.setText( "Due was "+ updateint);
        }

        upoccamount.setText(upamountint);
        upoccdesc.setText(updescriptionint);
        upoccdate.setText(updateint);
        upocctime.setText(uptimeint);


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
                        upoccdate.setText(singletext);
                        upoccdateshow.setText("Your reminder start at "+singletext);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );


        upocccalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(updateoccurance.this, android.R.layout.simple_list_item_1,arrlist);
        upoccuringtime.setAdapter(arrayAdapter);
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).equalsIgnoreCase(upocctypeint)) {
                upoccuringtime.setSelection(i);
                break;
            }
        }

        upoccuringtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerselecteditem=arrlist.get(i);
                if(isclickspinner){
                    upoccduedate.setVisibility(View.VISIBLE);
                }
                upoccperiodshow.setText("Occurrence time is "+spinnerselecteditem);
                try {
                    upoccduedate.setText("Next due is "+spinnerdate(spinnerselecteditem,upoccdate.getText().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        upoccbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        upocctimeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(updateoccurance.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                Calendar selectedTime = Calendar.getInstance();
                                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedTime.set(Calendar.MINUTE, minute);
                                String selectedTimeString = timeFormat.format(selectedTime.getTime());

                                upocctime.setText(selectedTimeString);
                            }
                        }, 10, 0, false);
                timePickerDialog.show();
            }
        });


        upoccpaidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(updateoccurance.this);
                builder.setTitle("Paid ?");
                builder.setMessage("Confirmation of paid.");
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("occurance")
                                .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                                .equalTo(Long.parseLong(uptimestampint)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                                if(snapshot1.exists() && Objects.equals(snapshot1.child("timestamp").getValue(), Long.parseLong(uptimestampint))){
                                                    String snap=snapshot1.getKey();
                                                    String updateddate= null;
                                                    try {
                                                        updateddate = spinnerdate(spinnerselecteditem,upoccdate.getText().toString());
                                                    } catch (ParseException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    FirebaseDatabase.getInstance().getReference().child("occurance")
                                                            .child(FirebaseAuth.getInstance().getUid()).child(snap).child("date").setValue(updateddate);
                                                    upanimpaid.setVisibility(View.VISIBLE);
                                                    upanimpaid.playAnimation();
                                                    upoccdateshow.setText("");
                                                    upoccduedate.setText("");
                                                    upoccperiodshow.setText("");
                                                    isclickspinner=false;
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            upanimpaid.cancelAnimation();
                                                            finish();
                                                            upanimpaid.setVisibility(View.INVISIBLE);
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
        upoccsubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upoccamount.getText().toString().isEmpty()){
                    upoccamount.requestFocus();
                    upoccamount.setError("Amount not be empty");
                }else if (upoccdesc.getText().toString().isEmpty()){
                    upoccdesc.requestFocus();
                    upoccdesc.setError("Title not be empty");
                }else if(upoccdesc.getText().toString().length()>15){
                    upoccdesc.requestFocus();
                    upoccdesc.setError("Title size must be less then 16");
                } else{
                    FirebaseDatabase.getInstance().getReference().child("occurance")
                            .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                            .equalTo(Long.parseLong(uptimestampint)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            String snap=snapshot1.getKey();

                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                                            LocalDate futureDate = LocalDate.parse(upoccdate.getText().toString(), formatter);
                                            LocalDate today = LocalDate.now();
                                            long daysBetween = ChronoUnit.DAYS.between(today, futureDate);

                                            Map<String,Object> map=new HashMap<>();
                                            map.put("amount",upoccamount.getText().toString());
                                            map.put("description",upoccdesc.getText().toString());
                                            map.put("date",upoccdate.getText().toString());
                                            map.put("time",upocctime.getText().toString());
                                            map.put("occtime",spinnerselecteditem);
                                            FirebaseDatabase.getInstance().getReference().child("occurance")
                                                    .child(FirebaseAuth.getInstance().getUid()).child(snap).updateChildren(map);
                                            Toast.makeText(updateoccurance.this, "Reminder updated sucessfully", Toast.LENGTH_SHORT).show();
                                            upoccdateshow.setText("");
                                            upoccduedate.setText("");
                                            upoccperiodshow.setText("");
                                            isclickspinner=false;
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
        upoccdeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(updateoccurance.this);
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure you want to delete reminder?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("occurance")
                                .child(FirebaseAuth.getInstance().getUid()).orderByChild("timestamp")
                                .equalTo(Long.parseLong(uptimestampint)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                                if(snapshot1.exists() && snapshot1.child("timestamp").getValue().equals(Long.parseLong(uptimestampint))){
                                                    Log.e("valuee",String.valueOf(snapshot1.getKey()));
                                                    snapshot1.getRef().removeValue();
                                                    upoccdateshow.setText("");
                                                    upoccduedate.setText("");
                                                    upoccperiodshow.setText("");
                                                    isclickspinner=false;
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
    private String spinnerdate(String spinnerselecteditem,String userdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date userInputDate = sdf.parse(userdate);
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
                calendar.add(Calendar.MONTH, 6);
                break;
            case "Yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
        }

        return sdf.format(calendar.getTime());
    }
}