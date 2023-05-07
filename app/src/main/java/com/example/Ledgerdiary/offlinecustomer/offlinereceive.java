package com.example.Ledgerdiary.offlinecustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class offlinereceive extends AppCompatActivity {
    CircleImageView addbackbtn;
    ImageView addcallbtn,calender;
    TextView addciname,adddate;
    EditText addamount,adddescription;
    Button addsave;

    String number,name,index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offlinereceive);
        addbackbtn=findViewById(R.id.addbackbtn);
        addcallbtn=findViewById(R.id.addcallbtn);
        calender=findViewById(R.id.calander);
        addciname=findViewById(R.id.addciname);
        addamount=findViewById(R.id.addamount);
        adddescription=findViewById(R.id.adddescription);
        addsave=findViewById(R.id.addsave);
        adddate=findViewById(R.id.adddate);


        number=getIntent().getStringExtra("onnumber");
        name=getIntent().getStringExtra("onname");
        index=getIntent().getStringExtra("onindex");

        addciname.setText(name);

        Calendar mcalendar = Calendar.getInstance();
        int initialYear = mcalendar.get(Calendar.YEAR);
        int initialMonth = mcalendar.get(Calendar.MONTH);
        int initialDay = mcalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date date = calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(date);
                        adddate.setText(formattedDate);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String defaultDate = sdf.format(calendar.getTime());
        adddate.setText(defaultDate);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date=Calendar.getInstance().getTime();
                datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                datePickerDialog.show();
            }
        });

        addbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addamount.getText().toString().isEmpty()){
                    addamount.setError("Amount is required");
                    addamount.requestFocus();
                }else{
                    Date date=new Date(new Date().getTime());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String formattedTime = dateFormat.format(date);
                    offlineDb db= Room.databaseBuilder(getApplicationContext(),offlineDb.class,"offlineusers").allowMainThreadQueries().build();
                    transactionDao dao= (transactionDao) db.transactionDao();
                    if(adddescription.getText().toString().isEmpty()){
                        adddescription.setText("transaction");
                    }
                    dao.insertdata(new offlinetransactions(Integer.parseInt(index),Integer.parseInt(addamount.getText().toString()),adddescription.getText().toString(),
                            adddate.getText().toString(),formattedTime,false));
                    finish();
                }
            }
        });

    }
}