package com.example.Ledgerdiary.reminder;

public class singletimeentrymodel {
    String amount,description,date,time;
    long timestamp;

    public singletimeentrymodel() {
    }

    public singletimeentrymodel(String amount, String description, String date, String time, long timestamp) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
