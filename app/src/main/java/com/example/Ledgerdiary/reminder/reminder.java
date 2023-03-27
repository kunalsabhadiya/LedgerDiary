package com.example.Ledgerdiary.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.dashboardFragment.fragmentadepter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class reminder extends AppCompatActivity {
    ViewPager remviewPager;
    TabLayout remtabLayout;
    FloatingActionButton remplus;
    ImageView remoccurance,remsingleentry;
    TextView tvaddoccurance,tvaddsingleentry;
    boolean isfabclicked=true;

    Animation upside,downside,fabopen,fabclose,rightside,leftside,blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        remviewPager=findViewById(R.id.remview);
        reminderfragmentadepter remfragmentadepter=new reminderfragmentadepter(getSupportFragmentManager());
        remviewPager.setAdapter(remfragmentadepter);
        remtabLayout=findViewById(R.id.remtablayout);
        remtabLayout.setupWithViewPager(remviewPager);

        remplus=findViewById(R.id.remplus);
        remoccurance=findViewById(R.id.remoccurancebtn);
        remsingleentry=findViewById(R.id.remsingleentrybtn);
        tvaddoccurance=findViewById(R.id.tvaddoccurance);
        tvaddsingleentry=findViewById(R.id.tvaddsingleentry);

        upside= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_bottom);
        downside= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.to_bottom);
        fabopen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_add_btn_open);
        fabclose= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_add_btn_close);
        rightside= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_slide_right);
        leftside= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_slide_left);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.buttonbehaviour);

        remplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfabclicked){
                    remsingleentry.setVisibility(View.VISIBLE);
                    remsingleentry.startAnimation(upside);
                    remoccurance.setVisibility(View.VISIBLE);
                    remoccurance.startAnimation(upside);
                    tvaddsingleentry.setVisibility(View.VISIBLE);
                    tvaddsingleentry.startAnimation(leftside);
                    tvaddoccurance.setVisibility(View.VISIBLE);
                    tvaddoccurance.startAnimation(leftside);
                    remplus.startAnimation(fabopen);
                    isfabclicked=false;
                }else{
                    remsingleentry.setVisibility(View.INVISIBLE);
                    remsingleentry.startAnimation(downside);
                    remoccurance.setVisibility(View.INVISIBLE);
                    remoccurance.startAnimation(downside);
                    tvaddsingleentry.setVisibility(View.INVISIBLE);
                    tvaddoccurance.setVisibility(View.INVISIBLE);
                    remplus.startAnimation(fabclose);
                    isfabclicked=true;
                }
            }
        });
        remsingleentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remsingleentry.startAnimation(blink);
                startActivity(new Intent(getApplicationContext(),addsingletimeentry.class));
            }
        });
        remoccurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoccurance.startAnimation(blink);
                startActivity(new Intent(getApplicationContext(),addoccurance.class));
            }
        });

    }
}