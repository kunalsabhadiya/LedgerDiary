package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class customerInterface extends AppCompatActivity {
TextView ciname,citotal;
CircleImageView backbtn,ciprofile;
ImageView givemoney,chat,pdf,cicall;
RelativeLayout expense;
LinearLayout editprofile;
RecyclerView rctransaction;
ArrayList<transactionmodel> transactionlist;
View overlayView;
Animation blink;
ProgressBar pg;
int famount=0;
transactionadepter transactionadepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_interface);

        ciname=findViewById(R.id.ciname);
        cicall=findViewById(R.id.callbtn);

        citotal=findViewById(R.id.citotal);
        backbtn=findViewById(R.id.backbtn);
        ciprofile=findViewById(R.id.ciprofile);

        editprofile=findViewById(R.id.editprofile);

        givemoney=findViewById(R.id.givemoney);
        chat=findViewById(R.id.chat);
        pdf=findViewById(R.id.pdf);

        expense=findViewById(R.id.expense);

        rctransaction=findViewById(R.id.rctransaction);
         blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.buttonbehaviour);

        pg=findViewById(R.id.transactionpg);

        String name=getIntent().getStringExtra("ciname");
        String number=getIntent().getStringExtra("cnumber");
        String imguri=getIntent().getStringExtra("oimageuri");
        String reciveruid=getIntent().getStringExtra("reciveruid");
        String senderroom = FirebaseAuth.getInstance().getUid()+reciveruid;
        overlayView = LayoutInflater.from(this).inflate(R.layout.backgroundoverlay, null);
        addContentView(overlayView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setVisibility(View.GONE);

        ciname.setText(name);
        Picasso.get().load(imguri).placeholder(R.drawable.profileimage).into(ciprofile);


        transactionlist =new ArrayList<>();
        transactionadepter=new transactionadepter(this, transactionlist, citotal, reciveruid, new transactionadepter.OnAdepterInterraction() {
            @Override
            public void onTouchListner(int position) {
                famount=0;
            }
        });


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rctransaction.setLayoutManager(linearLayoutManager);

       pdf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               pdf.startAnimation(blink);
               pdfGeneratorFragment fragment=new pdfGeneratorFragment();
               Bundle args = new Bundle();
               args.putString("chatname",name);
               args.putString("chatnumber",number);
               args.putString("receiveruid",reciveruid);
               fragment.setArguments(args);
               fragment.show(getSupportFragmentManager(), fragment.getTag());
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
               overlayView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       overlayView.setVisibility(View.GONE);
                   }
               });
               Picasso.get().load(imguri).placeholder(R.drawable.profileimage).into(imageView);
                   editTextNewName.setText(name);
                buttonSave.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String newName = editTextNewName.getText().toString();
                       FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                                       .child("customer").child(number).child("Cname").setValue(newName);
                       popupWindow.dismiss();
                       overlayView.setVisibility(View.GONE);
                       ciname.setText(newName);
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




       chat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               chat.startAnimation(blink);
               Intent intent=new Intent(customerInterface.this,chatactivity.class);
               intent.putExtra("chatimage",imguri);
               intent.putExtra("chatname",name);
               intent.putExtra("chatnumber",number);
               intent.putExtra("receiveruid",reciveruid);
               startActivity(intent);
           }
       });

        pg.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("transactions")
                .child(senderroom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionlist.clear();
                        famount=0;
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            transactionmodel model=snapshot1.getValue(transactionmodel.class);
                            transactionlist.add(model);
                            if(model.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                famount=famount+Integer.parseInt(model.getAmount());
                            }else{
                                famount=famount-Integer.parseInt(model.getAmount());
                            }
                        }
                        if(famount <0){
                            int am=Math.abs(famount);
                            String fl="You will give : "+am+" ₹";
                            citotal.setText(fl);
                            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                        }else if(famount ==0){
                            String fe="all due clear : "+famount+" ₹";
                            citotal.setText(fe);
                            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

                        }else{

                            String fm="You will get : "+famount+" ₹";
                            citotal.setText(fm);
                            citotal.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.gradcolor1));
                        }
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                                .child("customer").child(number)
                                .child("Ctamount").setValue(famount);
                        transactionadepter.notifyDataSetChanged();
                        pg.setVisibility(View.INVISIBLE);
                        rctransaction.scrollToPosition(transactionadepter.getItemCount() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        rctransaction.setAdapter(transactionadepter);
        cicall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cicall.startAnimation(blink);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                backbtn.startAnimation(blink);
                finish();
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.startAnimation(blink);
                Intent intent=new Intent(customerInterface.this, addTransaction.class);
                intent.putExtra("ciname",name);
                intent.putExtra("ciprofile",imguri);
                intent.putExtra("mnumber",number);
                intent.putExtra("ruid",reciveruid);
                famount=0;
                startActivity(intent);
            }
        });




    }


}