package com.example.Ledgerdiary.offlinecustomer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class citable {

        @PrimaryKey(autoGenerate = true)
        public int ciuid;

        @ColumnInfo(name = "ciname")
        public String ciname;

        @ColumnInfo(name = "ciphonenumber")
        public String ciphonenumber;

        @ColumnInfo(name = "citamount")
        public int citamount;


        public citable( String ciname, String ciphonenumber,int citamount) {
                this.ciname = ciname;
                this.ciphonenumber = ciphonenumber;
                this.citamount = citamount;

        }

        public int getCitamount() {
                return citamount;
        }

        public void setCitamount(int citamount) {
                this.citamount = citamount;
        }

        public int getCiuid() {
                return ciuid;
        }

        public void setCiuid(int ciuid) {
                this.ciuid = ciuid;
        }

        public String getCiname() {
                return ciname;
        }

        public void setCiname(String ciname) {
                this.ciname = ciname;
        }

        public String getCiphonenumber() {
                return ciphonenumber;
        }

        public void setCiphonenumber(String ciphonenumber) {
                this.ciphonenumber = ciphonenumber;
        }
}
