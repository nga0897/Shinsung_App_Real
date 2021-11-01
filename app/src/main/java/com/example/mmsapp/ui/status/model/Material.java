package com.example.mmsapp.ui.status.model;

import com.google.gson.annotations.SerializedName;

public class Material {
    @SerializedName("SD")
    private String sdNo;

    @SerializedName("trangthai")
    private String status;

    @SerializedName("lct_cd")
    private String location;

    @SerializedName("process")
    private String process;

    @SerializedName("product")
    private String product;

    @SerializedName("po")
    private String po;

    @SerializedName("ketluan")
    private String conclude;

    @SerializedName("sanluong")
    private String quantity;

    @SerializedName("time_mapping")
    private String timeMapping;

    public Material(String sdNo, String status, String location, String process, String product, String po, String conclude, String quantity, String timeMapping) {
        this.sdNo = sdNo;
        this.status = status;
        this.location = location;
        this.process = process;
        this.product = product;
        this.po = po;
        this.conclude = conclude;
        this.quantity = quantity;
        this.timeMapping = timeMapping;
    }

    public String getSdNo() {
        return sdNo;
    }

    public void setSdNo(String sdNo) {
        this.sdNo = sdNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getConclude() {
        return conclude;
    }

    public void setConclude(String conclude) {
        this.conclude = conclude;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTimeMapping() {
        return timeMapping;
    }

    public void setTimeMapping(String timeMapping) {
        this.timeMapping = timeMapping;
    }
}
