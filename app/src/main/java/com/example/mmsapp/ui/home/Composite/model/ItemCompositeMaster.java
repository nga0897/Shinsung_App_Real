package com.example.mmsapp.ui.home.Composite.model;

import com.google.gson.annotations.SerializedName;

public class ItemCompositeMaster {

    @SerializedName("mno")
    private int mNo;

    @SerializedName("mc_type")
    private String mcType;

    @SerializedName("mc_no")
    private String mcNo;

    @SerializedName("mc_nm")
    private String mcNm;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("color")
    private String color;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("re_mark")
    private String remark;

    @SerializedName("use_yn")
    private String useYn;

    @SerializedName("del_yn")
    private String delYn;

    @SerializedName("reg_id")
    private String regId;

    @SerializedName("chg_id")
    private String chgId;

    @SerializedName("chg_dt")
    private String chgDt;

    @SerializedName("su_dung")
    private String using;

/*    @SerializedName("RowNum")
    private int rowNum;*/


    public ItemCompositeMaster( int mNo, String mcType, String mcNo, String mcNm, String purpose, String color, String barcode, String remark, String useYn, String delYn, String regId, String chgId, String chgDt, String using/*, int rowNum*/) {
        this.mNo = mNo;
        this.mcType = mcType;
        this.mcNo = mcNo;
        this.mcNm = mcNm;
        this.purpose = purpose;
        this.color = color;
        this.barcode = barcode;
        this.remark = remark;
        this.useYn = useYn;
        this.delYn = delYn;
        this.regId = regId;
        this.chgId = chgId;
        this.chgDt = chgDt;
        this.using = using;
//        this.rowNum = rowNum;
    }

    public int getmNo() {
        return mNo;
    }

    public void setmNo(int mNo) {
        this.mNo = mNo;
    }

    public String getMcType() {
        return mcType;
    }

    public void setMcType(String mcType) {
        this.mcType = mcType;
    }

    public String getMcNo() {
        return mcNo;
    }

    public void setMcNo(String mcNo) {
        this.mcNo = mcNo;
    }

    public String getMcNm() {
        return mcNm;
    }

    public void setMcNm(String mcNm) {
        this.mcNm = mcNm;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getDelYn() {
        return delYn;
    }

    public void setDelYn(String delYn) {
        this.delYn = delYn;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getChgId() {
        return chgId;
    }

    public void setChgId(String chgId) {
        this.chgId = chgId;
    }

    public String getChgDt() {
        return chgDt;
    }

    public void setChgDt(String chgDt) {
        this.chgDt = chgDt;
    }

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    /*public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }*/
}
