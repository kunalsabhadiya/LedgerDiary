
package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateTransaction extends AppCompatActivity {
    CircleImageView upbackbtn;
    EditText upamount,updescription;
    TextView update;
    Button updelete,upupdate;
    ImageView upcalander;
    String uptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_transaction);
        upbackbtn=findViewById(R.id.upbackbtn);

        upamount=findViewById(R.id.upamount);
        updescription=findViewById(R.id.updescription);

        update=findViewById(R.id.update);

        updelete=findViewById(R.id.updelete);
        upupdate=findViewById(R.id.upupdate);

        upcalander=findViewById(R.id.upcalander);

        String iamount = getIntent().getStringExtra("amount");
        String idescription=getIntent().getStringExtra("description");
        String datee=getIntent().getStringExtra("datee");
        String timestampp =getIntent().getStringExtra("timestamp");
        String senderroom =getIntent().getStringExtra("senderroom");

        upamount.setText(iamount);
        updescription.setText(idescription);
        update.setText(datee);

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

                        uptext = String.format("%02d %s", dayNumber, monthName);
                        update.setText(uptext);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
      upcalander.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              datePickerDialog.show();
          }
      });

        upbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String am = upamount.getText().toString();
                String da = update.getText().toString();
                String de = updescription.getText().toString();
            /*   FirebaseDatabase.getInstance().getReference()
                        .child("transactions").orderByChild("timestamp").equalTo(timestampp).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    childSnapshot.getRef().child("amount").setValue(am);
                                    childSnapshot.getRef().child("description").setValue(de);
                                    childSnapshot.getRef().child("date").setValue(da);
                                    Toast.makeText(updateTransaction.this, "Transaction updated sucessfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                */
            }

        });
        updelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance().getReference()
                        .child("transactions").orderByChild("timestamp").equalTo(timestampp).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String senderid=(String)childSnapshot.child("senderId").getValue();
                                    System.out.println("ok"+senderid);
                                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(senderid)) {
                                        childSnapshot.getRef().removeValue();
                                        Toast.makeText(updateTransaction.this, "Transaction deleted sucessfully", Toast.LENGTH_SHORT).show();
                                        Log.e("inner", "f");
                                    }
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
    }
