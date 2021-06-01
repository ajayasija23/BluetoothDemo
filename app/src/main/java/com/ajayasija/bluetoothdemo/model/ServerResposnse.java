package com.ajayasija.bluetoothdemo.model;

import com.google.gson.annotations.SerializedName;

public class ServerResposnse {


    @SerializedName("status")
    private String status;
    @SerializedName("result_code")
    private int resultCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
