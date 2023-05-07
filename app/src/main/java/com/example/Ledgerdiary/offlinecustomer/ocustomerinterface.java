package com.example.Ledgerdiary.offlinecustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ocustomerinterface extends AppCompatActivity {
Button offsend,offreceive;
TextView offciname,citotal;
LinearLayout editprofile;
ImageView offcallbtn;
View overlayView;
ProgressBar pg;
String phonenumber,name,index;
RecyclerView offrctransaction;

List<offlinetransactions> list;
offlineDb db;
transactionDao dao;
UserDao userDao;
CircleImageView offbackbtn;
int total=0;
ocustomerinterfaceadepter adepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocustomerinterface);
      offsend=findViewById(R.id.offsend);
      offreceive=findViewById(R.id.offreceive);
     pg=findViewById(R.id.transactionpg);
     offciname=findViewById(R.id.offciname);
     citotal=findViewById(R.id.citotal);
     offcallbtn=findViewById(R.id.offcallbtn);
     offbackbtn=findViewById(R.id.offbackbtn);
     editprofile=findViewById(R.id.editprofile);
        offrctransaction=findViewById(R.id.offrctransaction);

        phonenumber=getIntent().getStringExtra("phonenumber");
        name=getIntent().getStringExtra("name");
        index=getIntent().getStringExtra("index");

        overlayView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.backgroundoverlay, null);
        addContentView(overlayView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setVisibility(View.GONE);

        list=new ArrayList<>();

      db= Room.databaseBuilder(getApplicationContext(),offlineDb.class,"offlineusers").allowMainThreadQueries().build();
        dao= (transactionDao) db.transactionDao();

        list = dao.getTransactionsForCitable(Integer.parseInt(index));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        offrctransaction.setLayoutManager(linearLayoutManager);
        adepter = new ocustomerinterfaceadepter(list, getApplicationContext());
        offrctransaction.setAdapter(adepter);
        offrctransaction.scrollToPosition(adepter.getItemCount() - 1);
        adepter.notifyDataSetChanged();


        offciname.setText(name);
        for(int i=0;i<list.size();i++){
            offlinetransactions offlinetransactions=list.get(i);
           if(offlinetransactions.getSender()){
               total += offlinetransactions.getAmount();
           }else{
               total -=offlinetransactions.getAmount();
           }
        }


        if(total<0){
            int am=Math.abs(total);
            String fl="You will give : "+am+" ₹";
            citotal.setText(fl);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
        }else if(total ==0){
            String fe="all due clear : "+total+" ₹";
            citotal.setText(fe);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }else{
            String fm="You will get : "+total+" ₹";
            citotal.setText(fm);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
        }
       userDao = db.userDao();
        userDao.updatetAmount(Integer.parseInt(index),total);

      offsend.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(ocustomerinterface.this,offliensend.class);
              intent.putExtra("offname",name);
              intent.putExtra("offnumber",phonenumber);
              intent.putExtra("offindex",index);
              startActivity(intent);
          }
      });

        offreceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ocustomerinterface.this,offlinereceive.class);
                intent.putExtra("onname",name);
                intent.putExtra("onnumber",phonenumber);
                intent.putExtra("onindex",index);
                startActivity(intent);
            }
        });

        offbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        offcallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonenumber));
                startActivity(intent);
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        View popupView = getLayoutInflater().inflate(R.layout.popuo_changename, null);
                        PopupWindow popupWindow = new PopupWindow(
                                popupView,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                overlayView.setVisibility(View.GONE);
                            }
                        });

                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        popupWindow.setElevation(3);
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                        EditText editTextNewName = popupView.findViewById(R.id.ppnewname);
                        Button buttonSave = popupView.findViewById(R.id.ppsave);
                        Button buttonCancel = popupView.findViewById(R.id.ppcancle);
                        CircleImageView imageView=popupView.findViewById(R.id.ppprofile);
                overlayView.setVisibility(View.VISIBLE);
                       imageView.setImageResource(R.drawable.profileimage);
                        editTextNewName.setText(name);
                        buttonSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newName = editTextNewName.getText().toString();
                                userDao.updatename(Integer.parseInt(index),newName);
                                popupWindow.dismiss();
                                overlayView.setVisibility(View.GONE);
                                offciname.setText(newName);
                                Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"Username updated sucessfully",Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.lightgreen));
                                snackbar.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                                snackbar.show();

                            }
                        });

                        buttonCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                                overlayView.setVisibility(View.GONE);
                            }
                        });

                    }
                });


            }







    @Override
    protected void onResume() {
        super.onResume();
        list = dao.getTransactionsForCitable(Integer.parseInt(index));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        offrctransaction.setLayoutManager(linearLayoutManager);
        adepter = new ocustomerinterfaceadepter(list, getApplicationContext());
        offrctransaction.setAdapter(adepter);
        offrctransaction.scrollToPosition(adepter.getItemCount() - 1);
        adepter.notifyDataSetChanged();
        total=0;

        for(int i=0;i<list.size();i++){
            offlinetransactions offlinetransactions=list.get(i);
            if(offlinetransactions.getSender()){
                total += offlinetransactions.getAmount();
            }else{
                total -=offlinetransactions.getAmount();
            }
        }

        if(total<0){
            int am=Math.abs(total);
            String fl="You will give : "+am+" ₹";
            citotal.setText(fl);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
        }else if(total ==0){
            String fe="all due clear : "+total+" ₹";
            citotal.setText(fe);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        }else{
            String fm="You will get : "+total+" ₹";
            citotal.setText(fm);
            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
        }
        userDao = db.userDao();
        userDao.updatetAmount(Integer.parseInt(index),total);

    }
}