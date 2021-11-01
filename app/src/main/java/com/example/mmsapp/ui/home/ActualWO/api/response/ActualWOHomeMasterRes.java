package com.example.mmsapp.ui.home.ActualWO.api.response;

import com.example.mmsapp.ui.home.ActualWO.model.ActualWOHomeMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActualWOHomeMasterRes {
    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("records")
    private int records;

    @SerializedName("rows")
    private List<ActualWOHomeMaster> actualWOHomeMasterList;

    public ActualWOHomeMasterRes(int total, int page, int records, List<ActualWOHomeMaster> actualWOHomeMasterList) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.actualWOHomeMasterList = actualWOHomeMasterList;
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

    public List<ActualWOHomeMaster> getActualWOHomeMasterList() {
        return actualWOHomeMasterList;
    }

    public void setActualWOHomeMasterList(List<ActualWOHomeMaster> actualWOHomeMasterList) {
        this.actualWOHomeMasterList = actualWOHomeMasterList;
    }
}
