package com.example.Ledgerdiary.onlinecustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Ledgerdiary.R;

public class splashadd extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashadd);
        lottieAnimationView=findViewById(R.id.lottieesucessadd);
        lottieAnimationView.animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2500);
    }
}