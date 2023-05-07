package com.example.Ledgerdiary.dashboardFragment;


public class onlinemodel {

    String Cuid,Cphonenumber,Cname,Cimageuri;
    int Ctamount;

    public void setCuid(String cuid) {
        Cuid = cuid;
    }

    public void setCphonenumber(String cphonenumber) {
        Cphonenumber = cphonenumber;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public void setCimageuri(String cimageuri) {
        Cimageuri = cimageuri;
    }

    public void setCtamount(int ctamount) {
        Ctamount = ctamount;
    }

    public String getCuid() {
        return Cuid;
    }

    public String getCimageuri() {
        return Cimageuri;
    }

    public String getCphonenumber() {
        return Cphonenumber;
    }

    public String getCname() {
        return Cname;
    }

    public int getCtamount() {
        return Ctamount;
    }
}
