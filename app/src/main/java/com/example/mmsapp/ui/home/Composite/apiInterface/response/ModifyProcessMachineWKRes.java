package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.google.gson.annotations.SerializedName;

public class ModifyProcessMachineWKRes {
    @SerializedName("result")
    private int result;

    @SerializedName("message")
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
