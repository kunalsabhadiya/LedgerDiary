package com.example.Ledgerdiary.onlinecustomer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class pdfGeneratorFragment extends BottomSheetDialogFragment {
 TextView tvstartdate,tvenddate;
 Button getdata;
 ImageView startdate,enddate;
    String name,reciveruid,number;

 private Calendar startDate, endDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View  view= inflater.inflate(R.layout.fragment_pdf_generator, container, false);
       tvstartdate=view.findViewById(R.id.tvstartdate);
        tvenddate=view.findViewById(R.id.tvenddate);
        getdata=view.findViewById(R.id.getdata);
        startdate=view.findViewById(R.id.startdate);
        enddate=view.findViewById(R.id.enddate);

        if(getArguments()!=null){
            name= getArguments().getString("chatname");
            number= getArguments().getString("chatnumber");
             reciveruid= getArguments().getString("receiveruid");
        }

        String senderroom= FirebaseAuth.getInstance().getCurrentUser().getUid()+reciveruid;
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvenddate.getText().toString().isEmpty() && !tvstartdate.getText().toString().isEmpty()){
                    Intent intent=new Intent(getContext(),selectedtransactionview.class);
                    intent.putExtra("startdate",tvstartdate.getText().toString());
                    intent.putExtra("enddate",tvenddate.getText().toString());
                    intent.putExtra("senderroom",senderroom);
                    intent.putExtra("name",name);
                    intent.putExtra("number",number);
                    startActivity(intent);
                }
            }
        });


       return view;
    }
    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar currentDate = Calendar.getInstance();
        Date date=currentDate.getTime();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                if (isStartDate) {
                    startDate = selectedDate;
                    updateDateText(tvstartdate, startDate);
                    showDatePickerDialog(false);
                } else {
                    endDate = selectedDate;
                    if (endDate.before(startDate)) {
                        getdata.setVisibility(View.GONE);
                    } else {
                        updateDateText(tvenddate, endDate);
                    }
                }
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(date.getTime());

        datePickerDialog.show();
    }

    private void updateDateText(TextView textView, Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        textView.setText(sdf.format(date.getTime()));
    }
/*
    private Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

 */
}