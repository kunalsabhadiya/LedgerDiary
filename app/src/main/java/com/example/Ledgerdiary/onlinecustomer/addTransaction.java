package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Ledgerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class addTransaction extends AppCompatActivity {
CircleImageView addbackbtn,addciprofile;
EditText addamount,adddescription;
TextView addciname,adddatetv;
Button addsave;
ImageView calander,addcallbtn;
    String text;
String senderroom,reciverroom,usernumber,username,noti,senderuid,reciveruid,imguri,number,name,rtoken,sname,simage;
RelativeLayout addrlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        addbackbtn = findViewById(R.id.addbackbtn);
        addcallbtn = findViewById(R.id.addcallbtn);
        addciprofile = findViewById(R.id.addciprofile);

        addamount = findViewById(R.id.addamount);
        adddescription = findViewById(R.id.adddescription);

        addciname = findViewById(R.id.addciname);
        adddatetv = findViewById(R.id.adddate);

        addsave = findViewById(R.id.addsave);

        calander = findViewById(R.id.calander);

        addrlayout = findViewById(R.id.addrlayout);

        if(getIntent().hasExtra("noti")){
             name = getIntent().getStringExtra("ciname");
             number = getIntent().getStringExtra("mnumber");
             imguri = getIntent().getStringExtra("ciprofile");
             reciveruid = getIntent().getStringExtra("ruid");
             noti = getIntent().getStringExtra("noti");

        }else{
             name = getIntent().getStringExtra("ciname");
             number = getIntent().getStringExtra("mnumber");
             imguri = getIntent().getStringExtra("ciprofile");
             reciveruid = getIntent().getStringExtra("ruid");
        }

        senderuid = FirebaseAuth.getInstance().getUid();
        senderroom = senderuid + reciveruid;
        reciverroom = reciveruid + senderuid;

        usernumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().replace("+", "");
        addamount.requestFocus();
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(senderuid).child("username")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        username = snapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        addciname.setText(name);
        Picasso.get().load(imguri).placeholder(R.drawable.profileimage).into(addciprofile);

        addcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });


        addbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                            date = sdf.parse(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        mcalendar.setTime(date);
                        int dayNumber = mcalendar.get(Calendar.DAY_OF_MONTH);
                        int monthNumber = mcalendar.get(Calendar.MONTH);

                        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
                        String[] months = dfs.getMonths();
                        String monthName = months[monthNumber];

                        text = String.format("%02d %s", dayNumber, monthName);
                        adddatetv.setText(text);
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
        adddatetv.setText(defaultDate);
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Date date=Calendar.getInstance().getTime();
                datePickerDialog.getDatePicker().setMaxDate(date.getTime());
                datePickerDialog.show();
            }
        });


        addsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addamount.getText().toString().isEmpty()) {
                    addamount.setError("Amount is required");
                    addamount.requestFocus();
                } else {
                    String desc = adddescription.getText().toString();
                    if (desc.isEmpty()) {
                        desc = "Transaction";
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);

                    transactionmodel model = new transactionmodel(senderuid, desc, addamount.getText().toString(), date.getTime(), adddatetv.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("transactions")
                            .child(senderroom)
                            .push().setValue(model)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference().child("transactions")
                                            .child(reciverroom)
                                            .push().setValue(model)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(addTransaction.this, "Data added sucessfully", Toast.LENGTH_SHORT).show();
                                                    adddescription.setText("");
                                                    addamount.setText("");
                                                    FirebaseDatabase.getInstance().getReference().child("users")
                                                            .child(reciveruid).child("customer").orderByChild("Cphonenumber").equalTo(usernumber)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (!snapshot.exists()) {
                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                .child(reciveruid).child("customer")
                                                                                .child(usernumber).child("Cphonenumber").setValue(usernumber);
                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                .child(reciveruid).child("customer")
                                                                                .child(usernumber).child("Cname").setValue(username);
                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                .child(reciveruid).child("customer")
                                                                                .child(usernumber).child("Cuid").setValue(senderuid);
                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                .child(reciveruid).child("customer")
                                                                                .child(usernumber).child("Ctamount").setValue("0");
                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                .child(senderuid).child("imageUri").addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        FirebaseDatabase.getInstance().getReference().child("users")
                                                                                                .child(reciveruid).child("customer")
                                                                                                .child(usernumber).child("Cimageuri").setValue(snapshot.getValue().toString());
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                    startActivity(new Intent(addTransaction.this,splashadd.class));
                                                    gettoken(addamount.getText().toString(),reciverroom,name,number,imguri);
                                                    finish();
                                                }
                                            });
                                }
                            });




                }
            }
        });












        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }



    }
    private void gettoken(String amount, String roomid, String rname, String rnumber, String rimageuri) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.child(reciveruid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rtoken = Objects.requireNonNull(snapshot.child("token").getValue()).toString();
                sendNotification(amount, roomid, rname, rnumber, rimageuri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("gettoken", "Database Error: " + error.getMessage());
            }
        });

        usersRef.child(senderuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sname = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                simage = Objects.requireNonNull(snapshot.child("imageUri").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("gettoken", "Database Error: " + error.getMessage());
            }
        });
    }

    private void sendNotification(String amount, String roomid, String rname, String rnumber, String rimageuri) {
        JSONObject to = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("title", sname);
            data.put("message", "Transaction added: " + amount);
            data.put("image", simage);
            data.put("rimage", rimageuri);
            data.put("id", reciveruid);
            data.put("chatid", roomid);
            data.put("number", rnumber);
            data.put("name", rname);
            to.put("token", rtoken);
            to.put("data", data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", to, response -> {
            Log.d("notification", "Notification sent: " + response);
        }, error -> {
            Log.d("notification", "Notification send failed: " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=AAAApKKw8xo:APA91bFZPz8GdPRTs8rk5MMePoyV-9C0tOGSVa8hJqLepytnAmPYALiQrCr7sfsC7RElLylV_De4_ugTLGtdX6vnt-hKivFxGKOltCyarrftmE9Hwl04fFH86r9FG_q--UcGTCIzgrSx");
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }



}