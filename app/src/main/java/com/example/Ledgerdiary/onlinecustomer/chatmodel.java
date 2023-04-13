package com.example.Ledgerdiary.onlinecustomer;

public class chatmodel {
    String chatsenderid;
    long timestamp;
    String msg;
    String img;

    public chatmodel() {
    }

    public chatmodel(String chatsenderid, long timestamp, String msg, String img) {
        this.chatsenderid = chatsenderid;
        this.timestamp = timestamp;
        this.msg = msg;
        this.img = img;
    }

    public String getChatsenderid() {
        return chatsenderid;
    }

    public void setChatsenderid(String chatsenderid) {
        this.chatsenderid = chatsenderid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
