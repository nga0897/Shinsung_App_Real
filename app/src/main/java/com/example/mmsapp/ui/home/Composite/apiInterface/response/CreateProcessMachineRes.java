package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.google.gson.annotations.SerializedName;

public class CreateProcessMachineRes {

    @SerializedName("result")
    private  int result;

    @SerializedName("mc_no")
    private String mcNo;

    @SerializedName("update")
    private String update;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;


    public CreateProcessMachineRes(int result, String mcNo, String update, String start, String end) {
        this.result = result;
        this.mcNo = mcNo;
        this.update = update;
        this.start = start;
        this.end = end;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMcNo() {
        return mcNo;
    }

    public void setMcNo(String mcNo) {
        this.mcNo = mcNo;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
