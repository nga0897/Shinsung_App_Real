package com.example.mmsapp.ui.status.model;

import com.google.gson.annotations.SerializedName;

public class ListStatus {

    @SerializedName("gr_qty")
    private int grQty;

    @SerializedName("gr_qty_bf")
    private int grQtyBf;

    @SerializedName("process")
    private String process;

    @SerializedName("mt_type")
    private String mtType;

    @SerializedName("recevice_dt_tims")
    private String receviceDtTims;

    @SerializedName("mt_sts_nm")
    private String mtStsNm;

    @SerializedName("lct_nm")
    private String lctNm;

    @SerializedName("mt_cd")
    private String mtCd;

    @SerializedName("po")
    private String po;

    @SerializedName("product")
    private String product;

    @SerializedName("staff_id")
    private String staffId;

    @SerializedName("staff_nm")
    private String staffNm;

    public ListStatus(int grQty, int grQtyBf, String process, String mtType, String receviceDtTims, String mtStsNm, String lctNm, String mtCd, String po, String product, String staffId, String staffNm) {
        this.grQty = grQty;
        this.grQtyBf = grQtyBf;
        this.process = process;
        this.mtType = mtType;
        this.receviceDtTims = receviceDtTims;
        this.mtStsNm = mtStsNm;
        this.lctNm = lctNm;
        this.mtCd = mtCd;
        this.po = po;
        this.product = product;
        this.staffId = staffId;
        this.staffNm = staffNm;
    }

    public int getGrQty() {
        return grQty;
    }

    public void setGrQty(int grQty) {
        this.grQty = grQty;
    }

    public int getGrQtyBf() {
        return grQtyBf;
    }

    public void setGrQtyBf(int grQtyBf) {
        this.grQtyBf = grQtyBf;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getMtType() {
        return mtType;
    }

    public void setMtType(String mtType) {
        this.mtType = mtType;
    }

    public String getReceviceDtTims() {
        return receviceDtTims;
    }

    public void setReceviceDtTims(String receviceDtTims) {
        this.receviceDtTims = receviceDtTims;
    }

    public String getMtStsNm() {
        return mtStsNm;
    }

    public void setMtStsNm(String mtStsNm) {
        this.mtStsNm = mtStsNm;
    }

    public String getLctNm() {
        return lctNm;
    }

    public void setLctNm(String lctNm) {
        this.lctNm = lctNm;
    }

    public String getMtCd() {
        return mtCd;
    }

    public void setMtCd(String mtCd) {
        this.mtCd = mtCd;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffNm() {
        return staffNm;
    }

    public void setStaffNm(String staffNm) {
        this.staffNm = staffNm;
    }
}
