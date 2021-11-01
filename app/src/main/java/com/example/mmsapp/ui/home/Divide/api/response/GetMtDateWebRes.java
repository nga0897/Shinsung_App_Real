package com.example.mmsapp.ui.home.Divide.api.response;

import com.example.mmsapp.ui.home.Divide.model.DivideMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMtDateWebRes {
    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("records")
    private int records;

    @SerializedName("rows")
    private List<DivideMaster> divideMasterList;

    public GetMtDateWebRes(int total, int page, int records, List<DivideMaster> divideMasterList) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.divideMasterList = divideMasterList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<DivideMaster> getDivideMasterList() {
        return divideMasterList;
    }

    public void setDivideMasterList(List<DivideMaster> divideMasterList) {
        this.divideMasterList = divideMasterList;
    }
}
