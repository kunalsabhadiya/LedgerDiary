package com.example.Ledgerdiary.offlinecustomer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface UserDao {
 @Insert
    void insertuser(citable citable);

 @Query("select exists(select * from citable where ciphonenumber= :number)")
    Boolean userexist(String number);

 @Query("select * from citable")
 List<citable> getofflineuser();

 @Delete
 void deleteuser(citable citable);

 @Query("UPDATE citable SET citamount = :amount WHERE ciuid = :id")
 void updatetAmount(int id, int amount);

 @Query("UPDATE citable SET ciname = :ciname WHERE ciuid = :id")
    void updatename(int id,String ciname);
}
