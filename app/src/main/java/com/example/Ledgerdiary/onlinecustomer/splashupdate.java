package com.example.Ledgerdiary.onlinecustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Ledgerdiary.R;

public class splashupdate extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashupdate);
        lottieAnimationView=findViewById(R.id.lottieesucessupdate);
        lottieAnimationView.animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2500);
    }
}