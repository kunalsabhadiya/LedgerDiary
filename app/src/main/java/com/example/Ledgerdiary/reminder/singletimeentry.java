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


public class singletimeentry extends Fragment {
RecyclerView singlerc;
singletimeentryadepter singletimeentryadepter;
List<singletimeentrymodel> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_singletimeentry, container, false);
        singlerc=view.findViewById(R.id.singleentryrc);

        list=new ArrayList<>();
        singletimeentryadepter=new singletimeentryadepter(getContext(),list);
        singlerc.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("singlereminder")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            singletimeentrymodel model=snapshot1.getValue(singletimeentrymodel.class);
                            list.add(model);
                        }

                        Collections.sort(list, new Comparator<singletimeentrymodel>() {
                            DateFormat f = new SimpleDateFormat("dd MMM yyyy");

                            @Override
                            public int compare(singletimeentrymodel o1, singletimeentrymodel o2) {
                                try {
                                    Date d1 = f.parse(o1.getDate());
                                    Date d2 = f.parse(o2.getDate());
                                    return d1.compareTo(d2);
                                } catch (ParseException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }
                        });


                        singletimeentryadepter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        singlerc.setAdapter(singletimeentryadepter);



    return view;
    }
}