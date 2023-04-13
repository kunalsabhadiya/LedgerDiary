package com.example.Ledgerdiary.dashboardFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.sqliteDbhelper;

import java.util.ArrayList;

public class offlineuser extends Fragment {
    RecyclerView rv;
    ArrayList<String> c_name, c_number;
    offlineadepter customerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_offlineuser, container, false);
     rv=view.findViewById(R.id.offline_user_recyclerview);

        sqliteDbhelper  db=new sqliteDbhelper(getContext());
     Cursor cursor = db.readAllData();
      c_name=new ArrayList<>();
      c_number=new ArrayList<>();
       if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                c_name.add(cursor.getString(1));
                c_number.add(cursor.getString(2));

            }
        }

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        customerAdapter= new offlineadepter(getContext(),c_name, c_number);
        rv.setAdapter(customerAdapter);
        customerAdapter.notifyDataSetChanged();
        return view;
    }
}