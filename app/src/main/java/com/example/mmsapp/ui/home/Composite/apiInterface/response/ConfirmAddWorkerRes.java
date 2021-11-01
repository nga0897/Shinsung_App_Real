package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.google.gson.annotations.SerializedName;

public class ConfirmAddWorkerRes {

    @SerializedName("result")
    private int result;

    @SerializedName("staff_tp")
    private String staffTp;

    @SerializedName("staff_id")
    private String staffId;

    @SerializedName("update")
    private String update;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;


    public ConfirmAddWorkerRes(int result, String staffTp, String staffId, String update, String start, String end) {
        this.result = result;
        this.staffTp = staffTp;
        this.staffId = staffId;
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

    public String getStaffTp() {
        return staffTp;
    }

    public void setStaffTp(String staffTp) {
        this.staffTp = staffTp;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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
