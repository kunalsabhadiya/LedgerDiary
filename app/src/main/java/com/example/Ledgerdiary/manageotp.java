package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

public class manageotp extends AppCompatActivity {
    private EditText ot1, ot2, ot3, ot4, ot5, ot6;
    private Button verifybtn;
    TextView tvotp;


    FirebaseAuth auth;


    String phonenumber;
    String otpid;
    String otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);
        verifybtn = findViewById(R.id.verifybtn);
        ot1 = findViewById(R.id.ot1);
        ot2 = findViewById(R.id.ot2);
        ot3 = findViewById(R.id.ot3);
        ot4 = findViewById(R.id.ot4);
        ot5 = findViewById(R.id.ot5);
        ot6 = findViewById(R.id.ot6);
        tvotp=findViewById(R.id.tvotp);
        setOtpText();

        otpid = getIntent().getStringExtra("verificationId");
        phonenumber = getIntent().getStringExtra("number");
        new otpfetch(tvotp);
        otp=tvotp.getText().toString();
        auth = FirebaseAuth.getInstance();
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ot1.getText().toString().trim().isEmpty() || ot2.getText().toString().trim().isEmpty() || ot3.getText().toString().trim().isEmpty()
                        || ot4.getText().toString().trim().isEmpty() || ot5.getText().toString().trim().isEmpty() || ot6.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(getApplicationContext(), "Invalid otp", Toast.LENGTH_SHORT).show();
                    ot1.requestFocus();
                }
                otp = ot1.getText().toString() +
                        ot2.getText().toString() +
                        ot3.getText().toString() +
                        ot4.getText().toString() +
                        ot5.getText().toString() +
                        ot6.getText().toString();
                if (otpid != null ) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp);
                        signInWithPhoneAuthCredential(credential);

                    }
                }

        });

    }




    private void setOtpText() {
     ot1.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 ot2.requestFocus();
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
     ot2.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 ot3.requestFocus();
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
     ot3.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 ot4.requestFocus();
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
     ot4.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 ot5.requestFocus();
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });
     ot5.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!s.toString().trim().isEmpty()){
                 ot6.requestFocus();
             }
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
     });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (auth.getCurrentUser().getMetadata().getCreationTimestamp() == auth.getCurrentUser().getMetadata().getLastSignInTimestamp()) {
                                Toast.makeText(manageotp.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(manageotp.this, profile.class);
                                startActivity(intent);
                                finish();
                            } else {
                                startActivity(new Intent(manageotp.this, Dashboard.class));
                                finish();
                            }

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




}
