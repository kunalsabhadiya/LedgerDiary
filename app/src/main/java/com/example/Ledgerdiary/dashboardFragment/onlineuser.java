package com.example.Ledgerdiary.dashboardFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class onlineuser extends Fragment {

RecyclerView onlinerc;
DatabaseReference reference;
onlinercadepter adepter;
ArrayList<onlinemodel> list;
ProgressBar onlinepg;
int total=0,give=0,got=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_onlineuser, container, false);
       onlinepg=view.findViewById(R.id.onlinepg);
       onlinerc=view.findViewById(R.id.online_user_recyclerview);
       LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
       onlinerc.setHasFixedSize(true);
       list=new ArrayList<>();

       reference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("customer");
        onlinepg.setVisibility(View.VISIBLE);
        adepter=new onlinercadepter(getContext(),list);
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               list.clear();
               for (DataSnapshot snapshot1:snapshot.getChildren()){
                   onlinemodel model=snapshot1.getValue(onlinemodel.class);
                   list.add(model);
                   assert model != null;
                   int amount=model.getCtamount();
                   total+=amount;
                   if(amount<0){
                       give+=Math.abs(amount);
                   }else{
                       got+=amount;
                   }
                   System.out.println(amount);
               }
               adepter.notifyDataSetChanged();
               FirebaseDatabase.getInstance().getReference().child("users")
                       .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                       .child("give").setValue(give);
               FirebaseDatabase.getInstance().getReference().child("users")
                       .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                       .child("got").setValue(got);
               FirebaseDatabase.getInstance().getReference().child("users")
                       .child(FirebaseAuth.getInstance().getUid()).child("onlineamount")
                       .child("total").setValue(total);
               give=got=total=0;
               onlinepg.setVisibility(View.GONE);


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(getContext(), "faild to load.", Toast.LENGTH_SHORT).show();
           }
       });



        onlinerc.setLayoutManager(layoutManager);
        onlinerc.setAdapter(adepter);
    return view;

    }

}