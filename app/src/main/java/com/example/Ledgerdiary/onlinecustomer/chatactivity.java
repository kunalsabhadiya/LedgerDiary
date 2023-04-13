package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatactivity extends AppCompatActivity {
    CircleImageView chatback,chatprofileimage,chatcall;
    TextView chatname;
    ImageView chatsend;
    RecyclerView rcchat;
    EditText chatmessage;
    String senderimg;
    ArrayList<chatmodel> list;
    Animation blink;
    chatadapter chatadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        chatback=findViewById(R.id.chatback);
        chatprofileimage=findViewById(R.id.chatprofileimage);
        chatcall=findViewById(R.id.chatphone);
        chatname=findViewById(R.id.chatname);
        chatsend=findViewById(R.id.chatsend);
        rcchat=findViewById(R.id.rcchat);
        chatmessage=findViewById(R.id.chatmessage);

        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.buttonbehaviour);


        String chatname1=getIntent().getStringExtra("chatname");
        String chatnumber=getIntent().getStringExtra("chatnumber");
        String chatimage=getIntent().getStringExtra("chatimage");
        String reciveruid=getIntent().getStringExtra("receiveruid");
        String senderroom= FirebaseAuth.getInstance().getUid()+reciveruid;
        String receiverroom= reciveruid+FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("imageUri")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    senderimg=snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        chatback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatback.startAnimation(blink);
                finish();
            }
        });

        chatcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatcall.startAnimation(blink);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + chatnumber));
                startActivity(intent);
            }
        });

       chatname.setText(chatname1);
        Picasso.get().load(chatimage).placeholder(R.drawable.profileimage).into(chatprofileimage);

        list=new ArrayList<>();
        chatadapter=new chatadapter(getApplicationContext(),list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rcchat.setLayoutManager(linearLayoutManager);


        FirebaseDatabase.getInstance().getReference().child("chats").child(senderroom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for(DataSnapshot snapshot1:snapshot.getChildren()){
                                    chatmodel model=snapshot1.getValue(chatmodel.class);
                                    list.add(model);
                                }
                                chatadapter.notifyDataSetChanged();
                                rcchat.scrollToPosition(chatadapter.getItemCount() - 1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        rcchat.setAdapter(chatadapter);

       chatsend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             chatsend.startAnimation(blink);

               if(!chatmessage.getText().toString().isEmpty()){
                   Date date=new Date();
                   Map<String,Object> map=new HashMap<>();
                   map.put("chatsenderid",FirebaseAuth.getInstance().getUid());
                   map.put("timestamp",date.getTime());
                   map.put("msg",chatmessage.getText().toString());
                   map.put("img",senderimg);
                   chatmessage.setText("");

                   FirebaseDatabase.getInstance().getReference().child("chats").child(senderroom).push().updateChildren(map)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   FirebaseDatabase.getInstance().getReference().child("chats").child(receiverroom).push().updateChildren(map)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                               }
                                           });
                                   chatadapter.notifyDataSetChanged();
                                   rcchat.smoothScrollToPosition(chatadapter.getItemCount() - 1);
                               }
                           });
               }
           }
       });




    }
}