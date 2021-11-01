package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.google.gson.annotations.SerializedName;

public class DestroyPreviousResultRes {

    @SerializedName("pmid")
    private int pmId;

    @SerializedName("id_actual")
    private int idActual;

    @SerializedName("start_dt")
    private String startDt;

    @SerializedName("end_dt")
    private String endDt;

    @SerializedName("remark")
    private String remark;

    @SerializedName("mc_no")
    private String mcNo;

    @SerializedName("use_yn")
    private String useYn;

    @SerializedName("del_yn")
    private String delYn;

    @SerializedName("reg_id")
    private String regId;

    @SerializedName("reg_dt")
    private String regDt;

    @SerializedName("chg_id")
    private String chgId;

    @SerializedName("chg_dt")
    private String chgDt;


    public DestroyPreviousResultRes(int pmId, int idActual, String startDt, String endDt, String remark, String mcNo, String useYn, String delYn, String regId, String regDt, String chgId, String chgDt) {
        this.pmId = pmId;
        this.idActual = idActual;
        this.startDt = startDt;
        this.endDt = endDt;
        this.remark = remark;
        this.mcNo = mcNo;
        this.useYn = useYn;
        this.delYn = delYn;
        this.regId = regId;
        this.regDt = regDt;
        this.chgId = chgId;
        this.chgDt = chgDt;
    }


    public int getPmId() {
        return pmId;
    }

    public void setPmId(int pmId) {
        this.pmId = pmId;
    }

    public int getIdActual() {
        return idActual;
    }

    public void setIdActual(int idActual) {
        this.idActual = idActual;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMcNo() {
        return mcNo;
    }

    public void setMcNo(String mcNo) {
        this.mcNo = mcNo;
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

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
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
}
