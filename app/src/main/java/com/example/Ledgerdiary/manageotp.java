package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class manageotp extends AppCompatActivity {
private EditText otp;
private Button verifybtn;


FirebaseAuth auth;


String phonenumber;
String otpid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);
       otp=findViewById(R.id.otp);
       verifybtn=findViewById(R.id.verifybtn);
        otpid = getIntent().getStringExtra("verificationId");

        auth= FirebaseAuth.getInstance();

        requestsmspermission();
        new otpfetch().setEditText(otp);

        phonenumber= getIntent().getStringExtra("number");
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().length() <6){
                    Toast.makeText(manageotp.this, "plz enter 6 digit code.", Toast.LENGTH_SHORT).show();
                }else if(otpid != null){
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
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

                        } else {
                            Toast.makeText(manageotp.this, "sign in error", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
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