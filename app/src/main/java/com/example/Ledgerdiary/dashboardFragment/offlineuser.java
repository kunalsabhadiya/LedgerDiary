package com.example.Ledgerdiary.dashboardFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.offlinecustomer.UserDao;
import com.example.Ledgerdiary.offlinecustomer.citable;
import com.example.Ledgerdiary.offlinecustomer.offlineDb;

import java.util.ArrayList;
import java.util.List;

public class offlineuser extends Fragment {
    RecyclerView rv;
    List<citable> list;
    offlineadepter customerAdapter;
    UserDao dao;
    offlineDb db;
    SearchView onlinesearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_offlineuser, container, false);
     rv=view.findViewById(R.id.offline_user_recyclerview);
        onlinesearch=view.findViewById(R.id.onlinesearch);
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
                customerAdapter.filter(newText);
                return false;
            }

        });
     list=new ArrayList<>();
       db= Room.databaseBuilder(getContext(),offlineDb.class,"offlineusers").allowMainThreadQueries().build();
        dao=db.userDao();
        list=dao.getofflineuser();
        customerAdapter=new offlineadepter(getContext(),list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(customerAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dao=db.userDao();
        list=dao.getofflineuser();
        customerAdapter=new offlineadepter(getContext(),list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(customerAdapter);

    }
}