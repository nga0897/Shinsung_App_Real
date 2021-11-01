package com.example.mmsapp.ui.status.api;

import com.example.mmsapp.ui.status.model.ListStatus;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetStatusBobbinRes {
    @SerializedName("result")
    private boolean result;

    @SerializedName("message")
    private String message;

    @SerializedName("Data")
    private List<ListStatus> statusList;

    public GetStatusBobbinRes(boolean result, String message, List<ListStatus> statusList) {
        this.result = result;
        this.message = message;
        this.statusList = statusList;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ListStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<ListStatus> statusList) {
        this.statusList = statusList;
    }
}
