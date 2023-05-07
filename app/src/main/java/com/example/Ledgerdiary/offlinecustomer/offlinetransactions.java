package com.example.Ledgerdiary.offlinecustomer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity
    public class offlinetransactions {
        @PrimaryKey(autoGenerate = true)
        private int id;

        @ColumnInfo(name = "citable_id")
        private int citableId;

        @ColumnInfo(name = "amount")
        private int amount;

        @ColumnInfo(name = "description")
        private String description;

        @ColumnInfo(name = "date")
        private String date;

        @ColumnInfo(name = "time")
        private String time;

        @ColumnInfo(name = "sender")
        private boolean sender;



        public offlinetransactions() {

        }

        public offlinetransactions(int citableId, int amount, String description, String date, String time, boolean sender) {
            this.citableId = citableId;
            this.amount = amount;
            this.description = description;
            this.date = date;
            this.time = time;
            this.sender = sender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCitableId() {
            return citableId;
        }

        public void setCitableId(int citableId) {
            this.citableId = citableId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean getSender() {
            return sender;
        }

        public void setSender(boolean sender) {
            this.sender = sender;
        }


    }

