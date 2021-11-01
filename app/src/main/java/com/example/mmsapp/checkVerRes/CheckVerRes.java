package com.example.mmsapp.checkVerRes;

import com.google.gson.annotations.SerializedName;

public class CheckVerRes {

    @SerializedName("result")
    private boolean result;

    @SerializedName("versionnew")
    private boolean versionNew;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataRes dataRes;

    public CheckVerRes(boolean result, boolean versionNew, String message, DataRes dataRes) {
        this.result = result;
        this.versionNew = versionNew;
        this.message = message;
        this.dataRes = dataRes;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isVersionNew() {
        return versionNew;
    }

    public void setVersionNew(boolean versionNew) {
        this.versionNew = versionNew;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataRes getDataRes() {
        return dataRes;
    }

    public void setDataRes(DataRes dataRes) {
        this.dataRes = dataRes;
    }
}
