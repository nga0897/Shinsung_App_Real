package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.example.mmsapp.ui.home.Composite.model.ItemStaffMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListWorkerRes {
    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("records")
    private int records;

    @SerializedName("rows")
    private List<ItemStaffMaster> listItem;

    public ListWorkerRes(int total, int page, int records, List<ItemStaffMaster> listItem) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.listItem = listItem;
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

    public List<ItemStaffMaster> getListItem() {
        return listItem;
    }

    public void setListItem(List<ItemStaffMaster> listItem) {
        this.listItem = listItem;
    }
}
