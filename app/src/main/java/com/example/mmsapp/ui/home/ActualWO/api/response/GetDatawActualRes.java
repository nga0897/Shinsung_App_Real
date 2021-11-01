package com.example.mmsapp.ui.home.ActualWO.api.response;

import com.example.mmsapp.ui.home.Manufacturing.model.ActualWOMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDatawActualRes {
    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("records")
    private int records;

    @SerializedName("rows")
    private List<ActualWOMaster> listActualWOMaster;

    public GetDatawActualRes(int total, int page, int records, List<ActualWOMaster> listActualWOMaster) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.listActualWOMaster = listActualWOMaster;
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

    public List<ActualWOMaster> getListActualWOMaster() {
        return listActualWOMaster;
    }

    public void setListActualWOMaster(List<ActualWOMaster> listActualWOMaster) {
        this.listActualWOMaster = listActualWOMaster;
    }
}
