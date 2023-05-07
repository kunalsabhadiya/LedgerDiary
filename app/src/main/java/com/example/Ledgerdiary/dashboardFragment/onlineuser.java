package com.example.Ledgerdiary.dashboardFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.fixentriesfragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class onlineuser extends Fragment {

RecyclerView onlinerc;
DatabaseReference reference;
onlinercadepter adepter;
ImageView mixpdf;
ArrayList<onlinemodel> list;
SearchView onlinesearch;
ShimmerFrameLayout shimmerFramelayout;
LinearLayout linearLayout;
Animation blink;
int total=0,give=0,got=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_onlineuser, container, false);
       onlinerc=view.findViewById(R.id.online_user_recyclerview);
       onlinesearch=view.findViewById(R.id.onlinesearch);
       mixpdf=view.findViewById(R.id.mixpdf);
       shimmerFramelayout=view.findViewById(R.id.shimmer_view);
       linearLayout=view.findViewById(R.id.shimmerlinear);
        blink= AnimationUtils.loadAnimation(getContext(),R.anim.buttonbehaviour);

       //for set inner color of searchview text...
        EditText searchEditText = onlinesearch.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
       onlinesearch.clearFocus();

       onlinesearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
             adepter.filter(newText);
               return false;
           }

       });
       LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
       onlinerc.setHasFixedSize(true);
       list=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("customer");
        shimmerFramelayout.startShimmer();
        adepter=new onlinercadepter(getContext(),list);

        mixpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mixpdf.startAnimation(blink);
                fixentriesfragment fragment=new fixentriesfragment();
                fragment.show(getChildFragmentManager(), "bottomSheet");
            }
        });

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
               linearLayout.setVisibility(View.INVISIBLE);
               shimmerFramelayout.stopShimmer();



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