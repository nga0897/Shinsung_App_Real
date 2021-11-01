package com.example.mmsapp.ui.home.Divide.model;

import com.google.gson.annotations.SerializedName;

public class DivDetailMaster {
    @SerializedName("wmtid")
    private int wmtId;

    @SerializedName("date")
    private String date;

    @SerializedName("mt_cd")
    private String mtCd;

    @SerializedName("mt_no")
    private String mtNo;

    @SerializedName("gr_qty")
    private int grQty;

    @SerializedName("bbmp_sts_cd")
    private String bbmpStsCd;

    @SerializedName("mt_qrcode")
    private String mtQrCode;

    @SerializedName("lct_cd")
    private String lctCd;

    @SerializedName("bb_no")
    private String bbNo;

    @SerializedName("mt_barcode")
    private String mtBarcode;

    @SerializedName("chg_dt")
    private String chgDt;

    @SerializedName("real_qty")
    private int realQty;

    public DivDetailMaster(int wmtId, String date, String mtCd, String mtNo, int grQty, String bbmpStsCd, String mtQrCode, String lctCd, String bbNo, String mtBarcode, String chgDt, int realQty) {
        this.wmtId = wmtId;
        this.date = date;
        this.mtCd = mtCd;
        this.mtNo = mtNo;
        this.grQty = grQty;
        this.bbmpStsCd = bbmpStsCd;
        this.mtQrCode = mtQrCode;
        this.lctCd = lctCd;
        this.bbNo = bbNo;
        this.mtBarcode = mtBarcode;
        this.chgDt = chgDt;
        this.realQty = realQty;
    }

    public int getWmtId() {
        return wmtId;
    }

    public void setWmtId(int wmtId) {
        this.wmtId = wmtId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMtCd() {
        return mtCd;
    }

    public void setMtCd(String mtCd) {
        this.mtCd = mtCd;
    }

    public String getMtNo() {
        return mtNo;
    }

    public void setMtNo(String mtNo) {
        this.mtNo = mtNo;
    }

    public int getGrQty() {
        return grQty;
    }

    public void setGrQty(int grQty) {
        this.grQty = grQty;
    }

    public String getBbmpStsCd() {
        return bbmpStsCd;
    }

    public void setBbmpStsCd(String bbmpStsCd) {
        this.bbmpStsCd = bbmpStsCd;
    }

    public String getMtQrCode() {
        return mtQrCode;
    }

    public void setMtQrCode(String mtQrCode) {
        this.mtQrCode = mtQrCode;
    }

    public String getLctCd() {
        return lctCd;
    }

    public void setLctCd(String lctCd) {
        this.lctCd = lctCd;
    }

    public String getBbNo() {
        return bbNo;
    }

    public void setBbNo(String bbNo) {
        this.bbNo = bbNo;
    }

    public String getMtBarcode() {
        return mtBarcode;
    }

    public void setMtBarcode(String mtBarcode) {
        this.mtBarcode = mtBarcode;
    }

    public String getChgDt() {
        return chgDt;
    }

    public void setChgDt(String chgDt) {
        this.chgDt = chgDt;
    }

    public int getRealQty() {
        return realQty;
    }

    public void setRealQty(int realQty) {
        this.realQty = realQty;
    }
}
