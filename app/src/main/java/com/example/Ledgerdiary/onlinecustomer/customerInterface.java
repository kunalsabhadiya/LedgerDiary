package com.example.Ledgerdiary.onlinecustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.Ledgerdiary.R;
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
RecyclerView rctransaction;
ArrayList<transactionmodel> transactionlist;
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

        givemoney=findViewById(R.id.givemoney);
        chat=findViewById(R.id.chat);
        pdf=findViewById(R.id.pdf);

        expense=findViewById(R.id.expense);

        rctransaction=findViewById(R.id.rctransaction);

        pg=findViewById(R.id.transactionpg);

        String name=getIntent().getStringExtra("ciname");
        String number=getIntent().getStringExtra("cnumber");
        String imguri=getIntent().getStringExtra("oimageuri");
        String reciveruid=getIntent().getStringExtra("reciveruid");
        String senderroom = FirebaseAuth.getInstance().getUid()+reciveruid;

        ciname.setText(name);
        Picasso.get().load(imguri).placeholder(R.drawable.profileimage).into(ciprofile);


        transactionlist =new ArrayList<>();
        transactionadepter=new transactionadepter(this,transactionlist,citotal,reciveruid);
//      transactionadepter.setOnAdepterInterraction(new transactionadepter.OnAdepterInterraction() {
//           @Override
//           public void onTouchListner(int position) {
//               famount=0;
//           }
//       });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rctransaction.setLayoutManager(linearLayoutManager);



        pg.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("transactions")
                .child(senderroom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        transactionlist.clear();
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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    protected void onResume() {
        super.onResume();
        famount = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        famount=0;
    }
}