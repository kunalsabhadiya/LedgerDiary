package com.example.Ledgerdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class setting extends AppCompatActivity {
TextView settingname,settingeditprofile,settingcontactus,settingAboutus,settinglogout,settingreset;
CircleImageView settingimage;
Animation clickbtn;
String image,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingname =findViewById(R.id.settingname);
        settingeditprofile =findViewById(R.id.settingeditprofile);
        settingcontactus =findViewById(R.id.settingcontactus);
        settingAboutus =findViewById(R.id.settingAboutus);
        settinglogout =findViewById(R.id.settinglogout);
        settingreset =findViewById(R.id.settingreset);
        settingimage =findViewById(R.id.settingimage);
        clickbtn= AnimationUtils.loadAnimation(this,R.anim.buttonbehaviour);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    image=snapshot.child("imageUri").getValue(String.class);
                                    name=snapshot.child("username").getValue(String.class);
                                Picasso.get().load(image).placeholder(R.drawable.profileimage).into(settingimage);
                                settingname.setText(name);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        settingeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingeditprofile.startAnimation(clickbtn);
                startActivity(new Intent(setting.this,profile.class));
            }
        });




            }

}