package com.example.mmsapp.ui.home.Mapping.model;

import com.google.gson.annotations.SerializedName;

public class MappingDetailMaster {

   @SerializedName("wmmid")
    private int wmmId;

    @SerializedName("mt_lot")
    private String mtLot;

    @SerializedName("mt_cd")
    private String mtCd;

    @SerializedName("mt_type")
    private String mtType;

    @SerializedName("mapping_dt")
    private String mappingDt;

    @SerializedName("use_yn")
    private String useYn;

    @SerializedName("reg_dt")
    private String regDt;

    @SerializedName("gr_qty")
    private int grQty;

    @SerializedName("mt_no")
    private String mtNo;

    @SerializedName("bb_no")
    private String bbNo;

    @SerializedName("Description")
    private String desc;

    @SerializedName("Used")
    private int used;

    @SerializedName("Remain")
    private int remain;

    public MappingDetailMaster(int wmmId, String mtLot, String mtCd, String mtType, String mappingDt, String useYn, String regDt, int grQty, String mtNo, String bbNo, String desc, int used, int remain) {
        this.wmmId = wmmId;
        this.mtLot = mtLot;
        this.mtCd = mtCd;
        this.mtType = mtType;
        this.mappingDt = mappingDt;
        this.useYn = useYn;
        this.regDt = regDt;
        this.grQty = grQty;
        this.mtNo = mtNo;
        this.bbNo = bbNo;
        this.desc = desc;
        this.used = used;
        this.remain = remain;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWmmId() {
        return wmmId;
    }

    public void setWmmId(int wmmId) {
        this.wmmId = wmmId;
    }

    public String getMtLot() {
        return mtLot;
    }

    public void setMtLot(String mtLot) {
        this.mtLot = mtLot;
    }

    public String getMtCd() {
        return mtCd;
    }

    public void setMtCd(String mtCd) {
        this.mtCd = mtCd;
    }

    public String getMtType() {
        return mtType;
    }

    public void setMtType(String mtType) {
        this.mtType = mtType;
    }

    public String getMappingDt() {
        return mappingDt;
    }

    public void setMappingDt(String mappingDt) {
        this.mappingDt = mappingDt;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public int getGrQty() {
        return grQty;
    }

    public void setGrQty(int grQty) {
        this.grQty = grQty;
    }

    public String getMtNo() {
        return mtNo;
    }

    public void setMtNo(String mtNo) {
        this.mtNo = mtNo;
    }

    public String getBbNo() {
        return bbNo;
    }

    public void setBbNo(String bbNo) {
        this.bbNo = bbNo;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}
