package com.example.Ledgerdiary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.dashboardFragment.onlineuser;
import com.example.Ledgerdiary.onlinecustomer.customerInterface;
import com.example.Ledgerdiary.onlinecustomer.selectedtransactionview;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class fixentriesfragment extends BottomSheetDialogFragment {
        Button getdata;
        ImageView startdate,enddate;
        TextView tvstartdate,tvenddate;
        Animation blink;
        private Calendar startDate, endDate;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View  view= inflater.inflate(R.layout.fragment_fixentriesfragment, container, false);
            tvstartdate=view.findViewById(R.id.tvstartdate);
            tvenddate=view.findViewById(R.id.tvenddate);
            getdata=view.findViewById(R.id.getdata);
            startdate=view.findViewById(R.id.startdate);
            enddate=view.findViewById(R.id.enddate);
            blink= AnimationUtils.loadAnimation(getContext(),R.anim.buttonbehaviour);

            tvstartdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvstartdate.startAnimation(blink);
                    showDatePickerDialog(true);
                }
            });

            tvenddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvenddate.startAnimation(blink);
                    showDatePickerDialog(false);
                }
            });

            getdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!tvenddate.getText().toString().isEmpty() && !tvstartdate.getText().toString().isEmpty()){
                            Intent intent=new Intent(getContext(), userreport.class);
                            intent.putExtra("startdate",tvstartdate.getText().toString());
                            intent.putExtra("enddate",tvenddate.getText().toString());
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
            if(!isStartDate){
                datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
            }

            datePickerDialog.show();
        }

        private void updateDateText(TextView textView, Calendar date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            textView.setText(sdf.format(date.getTime()));
        }


}