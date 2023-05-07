
package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
        String senderroom = getIntent().getStringExtra("senderroom");
        String reciverroom = senderroom.substring(senderroom.length() - 28) + senderroom.substring(0, 28);

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

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        Date date = calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(date);
                        update.setText(formattedDate);
                    }
                },
                initialYear,
                initialMonth,
                initialDay
        );
      upcalander.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Date date=Calendar.getInstance().getTime();
              datePickerDialog.getDatePicker().setMaxDate(date.getTime());
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
                if( upamount.getText().toString().isEmpty()){
                    upamount.requestFocus();
                    upamount.setError("");
                }else if(upamount.getText().toString().equals("0")){
                    upamount.requestFocus();
                    upamount.setError("Are you want to delete entry");
                }
                else {
                    String am = upamount.getText().toString();
                    String da = update.getText().toString();
                    String de = updescription.getText().toString();
                    FirebaseDatabase.getInstance().getReference()
                            .child("transactions").child(senderroom).orderByChild("timestamp").equalTo(Long.parseLong(timestampp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        System.out.println("first :" + dataSnapshot.getKey());
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (snapshot.exists() && Objects.equals(snapshot.child("senderId").getValue(), FirebaseAuth.getInstance().getUid())) {
                                                System.out.println("secound :" + snapshot.getKey());
                                                FirebaseDatabase.getInstance().getReference().child("transactions")
                                                        .child(senderroom).child(Objects.requireNonNull(snapshot.getKey()))
                                                        .child("amount").setValue(am);
                                                FirebaseDatabase.getInstance().getReference().child("transactions")
                                                        .child(senderroom).child(Objects.requireNonNull(snapshot.getKey()))
                                                        .child("date").setValue(da);
                                                FirebaseDatabase.getInstance().getReference().child("transactions")
                                                        .child(senderroom).child(Objects.requireNonNull(snapshot.getKey()))
                                                        .child("description").setValue(de);


                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("transactions").child(reciverroom).orderByChild("timestamp").equalTo(Long.parseLong(timestampp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot idataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    for (DataSnapshot isnapshot : idataSnapshot.getChildren()) {
                                                                        if (isnapshot.exists() && Objects.equals(isnapshot.child("senderId").getValue(), FirebaseAuth.getInstance().getUid())) {
                                                                            FirebaseDatabase.getInstance().getReference().child("transactions")
                                                                                    .child(reciverroom).child(Objects.requireNonNull(isnapshot.getKey()))
                                                                                    .child("amount").setValue(am);
                                                                            FirebaseDatabase.getInstance().getReference().child("transactions")
                                                                                    .child(reciverroom).child(Objects.requireNonNull(isnapshot.getKey()))
                                                                                    .child("date").setValue(da);
                                                                            FirebaseDatabase.getInstance().getReference().child("transactions")
                                                                                    .child(reciverroom).child(Objects.requireNonNull(isnapshot.getKey()))
                                                                                    .child("description").setValue(de);
                                                                            startActivity(new Intent(updateTransaction.this,splashupdate.class));
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


        updelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("transactions").child(senderroom).orderByChild("timestamp")
                        .equalTo(Long.parseLong(timestampp)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                                        if(snapshot1.exists() && Objects.equals(snapshot1.child("senderId").getValue(), FirebaseAuth.getInstance().getUid())){
                                            snapshot1.getRef().removeValue();
                                            upamount.setText("");
                                            updescription.setText("");
                                            FirebaseDatabase.getInstance().getReference().child("transactions").child(reciverroom).orderByChild("timestamp")
                                                    .equalTo(Long.parseLong(timestampp)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot isnapshot) {
                                                            if(snapshot.exists()){
                                                                for(DataSnapshot isnapshot1:isnapshot.getChildren()){
                                                                    System.out.println("secound "+isnapshot1.getKey());
                                                                    if(isnapshot1.exists() && Objects.equals(isnapshot1.child("senderId").getValue(), FirebaseAuth.getInstance().getUid())){
                                                                        isnapshot1.getRef().removeValue();
                                                                        startActivity(new Intent(updateTransaction.this,splashdelete.class));
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
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });






        }


    }
