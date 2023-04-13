package com.example.Ledgerdiary.onlinecustomer;

public class pdfmodel {
    String senderId,amount,date,description;
    int total;

    public pdfmodel() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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

    public pdfmodel(String senderId, String amount, String date, String description) {
        this.senderId = senderId;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}
