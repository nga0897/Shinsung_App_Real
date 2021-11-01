package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.example.mmsapp.ui.home.Composite.model.CompositeMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoMcWkMoldRes {
    @SerializedName("data")
    private List<CompositeMaster> compositeMasterList;

    @SerializedName("result")
    private boolean result;

    @SerializedName("message")
    private String message;

    public InfoMcWkMoldRes(List<CompositeMaster> compositeMasterList, boolean result, String message) {
        this.compositeMasterList = compositeMasterList;
        this.result = result;
        this.message = message;
    }

    public List<CompositeMaster> getCompositeMasterList() {
        return compositeMasterList;
    }

    public void setCompositeMasterList(List<CompositeMaster> compositeMasterList) {
        this.compositeMasterList = compositeMasterList;
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
}
