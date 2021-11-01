package com.example.mmsapp.ui.home.Mapping.model;

import com.google.gson.annotations.SerializedName;

public class MappingMaster {

    @SerializedName("id_actual")
    private int idActual;

    @SerializedName("staff_id")
    private String staffId;

    @SerializedName("reg_dt")
    private String regDt;

    @SerializedName("wmtid")
    private int wmtId;

    @SerializedName("DATE")
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

    @SerializedName("bb_no")
    private String bbNo;

    @SerializedName("chg_dt")
    private String chgDt;

    @SerializedName("sl_tru_ng")
    private int slTruNg;

    @SerializedName("count_table2")
    private int count_table2;

    @SerializedName("het_ca")
    private int endShift;

    @SerializedName("Description")
    private String description;


    public MappingMaster(int idActual, String staffId, String regDt, int wmtId, String date, String mtCd, String mtNo, int grQty, String bbmpStsCd, String mtQrCode, String bbNo, String chgDt, int slTruNg, int count_table2, int endShift, String description) {
        this.idActual = idActual;
        this.staffId = staffId;
        this.regDt = regDt;
        this.wmtId = wmtId;
        this.date = date;
        this.mtCd = mtCd;
        this.mtNo = mtNo;
        this.grQty = grQty;
        this.bbmpStsCd = bbmpStsCd;
        this.mtQrCode = mtQrCode;
        this.bbNo = bbNo;
        this.chgDt = chgDt;
        this.slTruNg = slTruNg;
        this.count_table2 = count_table2;
        this.endShift = endShift;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdActual() {
        return idActual;
    }

    public void setIdActual(int idActual) {
        this.idActual = idActual;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
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

    public String getBbNo() {
        return bbNo;
    }

    public void setBbNo(String bbNo) {
        this.bbNo = bbNo;
    }

    public String getChgDt() {
        return chgDt;
    }

    public void setChgDt(String chgDt) {
        this.chgDt = chgDt;
    }

    public int getSlTruNg() {
        return slTruNg;
    }

    public void setSlTruNg(int slTruNg) {
        this.slTruNg = slTruNg;
    }

    public int getCount_table2() {
        return count_table2;
    }

    public void setCount_table2(int count_table2) {
        this.count_table2 = count_table2;
    }

    public int getEndShift() {
        return endShift;
    }

    public void setEndShift(int endShift) {
        this.endShift = endShift;
    }
}
