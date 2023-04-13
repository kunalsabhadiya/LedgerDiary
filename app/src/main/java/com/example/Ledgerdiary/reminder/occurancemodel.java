package com.example.Ledgerdiary.reminder;

public class occurancemodel {
String amount,date,description,time,occtime;
long timestamp;

    public occurancemodel() {
    }

    public occurancemodel(String amount, String date, String description, String time, String occtime, long timestamp) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.time = time;
        this.occtime = occtime;
        this.timestamp = timestamp;
    }

    public String getOcctime() {
        return occtime;
    }

    public void setOcctime(String occtime) {
        this.occtime = occtime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
