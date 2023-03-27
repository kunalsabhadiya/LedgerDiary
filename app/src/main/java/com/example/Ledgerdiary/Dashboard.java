package com.example.Ledgerdiary;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Ledgerdiary.dashboardFragment.fragmentadepter;

import com.example.Ledgerdiary.dashboardFragment.onlineuser;
import com.example.Ledgerdiary.group.addgroupentry;
import com.example.Ledgerdiary.reminder.reminder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    private ActivityResultLauncher<String[]> permissionLauncher;
    private boolean isReadPermissionGranted = false;
    private boolean isphonePermissionGranted = false;
    private boolean iscamaraPermissionGranted = false;

    Toolbar toolbar;
  ViewPager viewPager;
  TabLayout tabLayout;
  FloatingActionButton addentry,addperson,addgroup,setting,reminderbtn;
  Animation rotateopen,rotateclose,slideleft,slideright;
  Menu menu;
  Boolean isclicked=true;
  int got=0,give=0,total=0;
  TextView onlinetotal,reciveonline,sendonline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    isReadPermissionGranted = permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null ? Boolean.TRUE.equals(permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE)) : isReadPermissionGranted;
                    isphonePermissionGranted = permissions.get(Manifest.permission.READ_CONTACTS) != null ? Boolean.TRUE.equals(permissions.get(Manifest.permission.READ_CONTACTS)) : isphonePermissionGranted;
                    iscamaraPermissionGranted = permissions.get(Manifest.permission.CAMERA) != null ? Boolean.TRUE.equals(permissions.get(Manifest.permission.CAMERA)) : iscamaraPermissionGranted;
                }
        );


        requestPermission();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=findViewById(R.id.viewpage);
        fragmentadepter fragmentadepter=new fragmentadepter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentadepter);
        tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


        addentry=findViewById(R.id.addentry);
        addgroup=findViewById(R.id.addgroup);
        addperson=findViewById(R.id.addperson);
        setting=findViewById(R.id.setting);
        reminderbtn=findViewById(R.id.reminder);
        onlinetotal=findViewById(R.id.dashboardtotalonline);
        reciveonline=findViewById(R.id.onlinerecive);
        sendonline=findViewById(R.id.onlinegive);


        rotateopen= AnimationUtils.loadAnimation(this,R.anim.rotate_add_btn_open);
        rotateclose= AnimationUtils.loadAnimation(this,R.anim.rotate_add_btn_close);
        slideleft= AnimationUtils.loadAnimation(this,R.anim.rotate_slide_left);
        slideright= AnimationUtils.loadAnimation(this,R.anim.rotate_slide_right);

        FirebaseDatabase.getInstance().getReference().child("users")
                           .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                           .child("give").addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   sendonline.setText(String.valueOf(snapshot.getValue()));
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                   FirebaseDatabase.getInstance().getReference().child("users")
                           .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                           .child("got").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           reciveonline.setText(String.valueOf(snapshot.getValue()));
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
                   FirebaseDatabase.getInstance().getReference().child("users")
                           .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                           .child("total").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           onlinetotal.setText(String.valueOf(snapshot.getValue()));
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });







        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, addgroupentry.class));
            }
        });

        addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isclicked==true){
                    addgroup.show();
                    addgroup.startAnimation(slideleft);
                    addperson.show();
                    addperson.startAnimation(slideleft);
                    reminderbtn.show();
                    reminderbtn.startAnimation(slideright);
                    setting.show();
                    setting.startAnimation(slideright);
                    addentry.startAnimation(rotateopen);
                    isclicked=false;
                }else{
                    addentry.startAnimation(rotateclose);
                    addgroup.hide();
                    addgroup.startAnimation(slideright);
                    addperson.hide();
                    addperson.startAnimation(slideright);
                    reminderbtn.hide();
                    reminderbtn.startAnimation(slideleft);
                    setting.hide();
                    setting.startAnimation(slideleft);
                    isclicked=true;
                    
                }

            }
        });

        addperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,contactselecting.class));
            }
        });


      setting.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(Dashboard.this,profile.class));
          }
      });

      reminderbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(Dashboard.this, reminder.class));
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


    private void requestPermission(){

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        isphonePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED;

        iscamaraPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<>();

        if (!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!isphonePermissionGranted){
            permissionRequest.add(Manifest.permission.READ_CONTACTS);
        }

        if (!iscamaraPermissionGranted){
            permissionRequest.add(Manifest.permission.CAMERA);
        }

        if (!permissionRequest.isEmpty()){
            permissionLauncher.launch(permissionRequest.toArray(new String[0]));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu,menu);
        Drawable d=menu.getItem(0).getIcon();
        d.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.DST_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.add){
            startActivity(new Intent(Dashboard.this,contactselecting.class));
        }else if(id==R.id.logout){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout?")
                        .setTitle("Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(Dashboard.this,login.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

        }

        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}