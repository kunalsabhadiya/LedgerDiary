package com.example.Ledgerdiary.offlinecustomer;

import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.List;


@Database(entities = {citable.class,offlinetransactions.class},version = 2,exportSchema = false)
public abstract class offlineDb extends RoomDatabase {
public abstract UserDao userDao();
public abstract transactionDao transactionDao();


}
