package com.example.mmsapp.ui.home.ActualWO.model;

import com.google.gson.annotations.SerializedName;

public class ActualWOHomeMaster {

    @SerializedName("id_actualpr")
    private int idActualpr;

    @SerializedName("at_no")
    private String atNo;

    @SerializedName("type")
    private String type;

    @SerializedName("totalTarget")
    private int totalTarget;

    @SerializedName("target")
    private int target;

    @SerializedName("product")
    private String product;

    @SerializedName("remark")
    private String remark;

    @SerializedName("CountProcess")
    private int processCount;

    @SerializedName("actual")
    private int actual;

    @SerializedName("poRun")
    private int poRun;

    @SerializedName("md_cd")
    private String mdCd;

    @SerializedName("style_nm")
    private String styleNm;


    public ActualWOHomeMaster(int idActualpr, String atNo, String type, int totalTarget,
                              int target, String product, String remark, int processCount,
                              int actual, int poRun, String mdCd, String styleNm) {
        this.idActualpr = idActualpr;
        this.atNo = atNo;
        this.type = type;
        this.totalTarget = totalTarget;
        this.target = target;
        this.product = product;
        this.remark = remark;
        this.processCount = processCount;
        this.actual = actual;
        this.poRun = poRun;
        this.mdCd = mdCd;
        this.styleNm = styleNm;
    }

    public int getIdActualpr() {
        return idActualpr;
    }

    public void setIdActualpr(int idActualpr) {
        this.idActualpr = idActualpr;
    }

    public String getAtNo() {
        return atNo;
    }

    public void setAtNo(String atNo) {
        this.atNo = atNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalTarget() {
        return totalTarget;
    }

    public void setTotalTarget(int totalTarget) {
        this.totalTarget = totalTarget;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public int getPoRun() {
        return poRun;
    }

    public void setPoRun(int poRun) {
        this.poRun = poRun;
    }

    public String getMdCd() {
        return mdCd;
    }

    public void setMdCd(String mdCd) {
        this.mdCd = mdCd;
    }

    public String getStyleNm() {
        return styleNm;
    }

    public void setStyleNm(String styleNm) {
        this.styleNm = styleNm;
    }
}
