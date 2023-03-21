package com.example.Ledgerdiary.onlinecustomer;

public class transactionmodel {
    String senderId,description,amount,date;
    long timestamp;


    public transactionmodel() {
    }

    public transactionmodel(String senderId, String description, String amount, long timestamp,String date) {
        this.senderId = senderId;
        this.description = description;
        this.amount = amount;
        this.timestamp = timestamp;
        this.date = date;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
