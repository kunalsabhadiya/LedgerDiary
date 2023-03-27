package com.example.Ledgerdiary.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Ledgerdiary.Dashboard;
import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.Splash;
import com.example.Ledgerdiary.login;
import com.google.firebase.auth.FirebaseAuth;

public class splashaddgroupentry extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashaddgroupentry);
        lottieAnimationView=findViewById(R.id.lottieesucess);
        lottieAnimationView.animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    finish();
            }
        },2500);
    }
}