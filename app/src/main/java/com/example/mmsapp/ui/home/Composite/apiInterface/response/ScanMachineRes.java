package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.example.mmsapp.ui.home.Composite.model.ItemCompositeMaster;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanMachineRes {
    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("records")
    private int records;

    @SerializedName("rows")
    private List<ItemCompositeMaster> itemCompositeMasterList;


    public ScanMachineRes(int total, int page, int records, List<ItemCompositeMaster> itemCompositeMasterList) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.itemCompositeMasterList = itemCompositeMasterList;
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

    public List<ItemCompositeMaster> getItemCompositeMasterList() {
        return itemCompositeMasterList;
    }

    public void setItemCompositeMasterList(List<ItemCompositeMaster> itemCompositeMasterList) {
        this.itemCompositeMasterList = itemCompositeMasterList;
    }
}
