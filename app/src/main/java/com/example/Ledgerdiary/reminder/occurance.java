package com.example.Ledgerdiary.reminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class occurance extends Fragment {
RecyclerView occurancerc;
List<occurancemodel> list;
occuranceadepter occuranceadepter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_occurance, container, false);
       occurancerc=view.findViewById(R.id.occurancerc);

            list=new ArrayList<>();
        occuranceadepter=new occuranceadepter(getContext(),list);
        occurancerc.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseDatabase.getInstance().getReference().child("occurance")
                    .child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                occurancemodel model=snapshot1.getValue(occurancemodel.class);
                                list.add(model);
                            }

                            Collections.sort(list, new Comparator<occurancemodel>() {
                                DateFormat f = new SimpleDateFormat("dd MMM yyyy");

                                @Override
                                public int compare(occurancemodel o1, occurancemodel o2) {
                                    try {
                                        Date d1 = f.parse(o1.getDate());
                                        Date d2 = f.parse(o2.getDate());
                                        return d1.compareTo(d2);
                                    } catch (ParseException e) {
                                        throw new IllegalArgumentException(e);
                                    }
                                }
                            });


                            occuranceadepter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            occurancerc.setAdapter(occuranceadepter);



            return view;
    }
}