package com.vingcoz.srishticatering.models.api.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<LogindataItem> logindata;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LogindataItem> getLogindata() {
        return logindata;
    }

    public void setLogindata(List<LogindataItem> logindata) {
        this.logindata = logindata;
    }

    @Override
    public String toString() {
        return
                "LoginResponse{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        ",logindata = '" + logindata + '\'' +
                        "}";
    }
}