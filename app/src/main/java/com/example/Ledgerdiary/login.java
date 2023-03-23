package com.example.Ledgerdiary;
import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.PhoneAuthCredential;
        import com.google.firebase.auth.PhoneAuthOptions;
        import com.google.firebase.auth.PhoneAuthProvider;
        import com.hbb20.CountryCodePicker;

        import java.util.concurrent.TimeUnit;


public class login extends AppCompatActivity {
    private Button getotp;
    private CountryCodePicker ccp;
    private EditText mobilenumbtf ;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getotp = findViewById(R.id.getotp);
        mobilenumbtf = findViewById(R.id.mobilenumtf);
        progressBar = findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(mobilenumbtf);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        requestsmspermission();

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobilenumbtf.getText().toString().trim().isEmpty()) {
                    Toast.makeText(login.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (mobilenumbtf.getText().toString().replace(" ","").length() != 10) {
                    Toast.makeText(login.this, "Type valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    otpSend();
                }

            }
        });
    }
    private void otpSend() {
        progressBar.setVisibility(View.VISIBLE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                mobilenumbtf.setVisibility(View.VISIBLE);
                Toast.makeText(login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressBar.setVisibility(View.GONE);
                mobilenumbtf.setVisibility(View.VISIBLE);
                Toast.makeText(login.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(login.this, manageotp.class);
                intent.putExtra("phone", ccp.getFullNumberWithPlus().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(ccp.getFullNumberWithPlus().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void requestsmspermission() {
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this,smspermission);
        //check if read SMS permission is granted or not
        if(grant!= PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0]=smspermission;
            ActivityCompat.requestPermissions(this,permission_list,1);
        }
    }


}
