package com.example.Ledgerdiary.offlinecustomer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface transactionDao {

     @Query("SELECT * FROM offlinetransactions WHERE citable_id = :citableId")
     List<offlinetransactions> getTransactionsForCitable(int citableId);

     @Insert
    void insertdata(offlinetransactions offlinetransactions);
}
