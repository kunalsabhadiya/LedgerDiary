package com.example.Ledgerdiary.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.Ledgerdiary.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class addsingletimeentry extends AppCompatActivity {
EditText singleamount,singledescription;
ImageView singlecalander,singletimeimg;
CircleImageView singlebackbtn;
TextView singledate,singletime;
String singletext;
Button singlesave;

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
                        int dayNumber = mcalendar.get(Calendar.DAY_OF_MONTH);
                        int monthNumber = mcalendar.get(Calendar.MONTH);

                        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
                        String[] months = dfs.getMonths();
                        String monthName = months[monthNumber];

                        singletext = String.format("%02d %s", dayNumber, monthName);
                        singledate.setText(singletext);
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
        singledate.setText(defaultDate);

        singlecalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
}