package com.example.mmsapp.ui.home.Mapping.apiInterface.response;

import com.example.mmsapp.ui.home.Mapping.model.MappingMaster;
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
    private List<MappingMaster> mappingMasterList;

    public GetMtDateWebRes(int total, int page, int records, List<MappingMaster> mappingMasterList) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.mappingMasterList = mappingMasterList;
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

    public List<MappingMaster> getMappingMasterList() {
        return mappingMasterList;
    }

    public void setMappingMasterList(List<MappingMaster> mappingMasterList) {
        this.mappingMasterList = mappingMasterList;
    }
}
