package com.vingcoz.srishticatering.models.api.login;

import com.google.gson.annotations.SerializedName;

public class LogindataItem {

    @SerializedName("Mobile No.")
    private String mobileNo;

    @SerializedName("ID")
    private int iD;

    @SerializedName("Name")
    private String name;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "LogindataItem{" +
                        "mobile No. = '" + mobileNo + '\'' +
                        ",iD = '" + iD + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}