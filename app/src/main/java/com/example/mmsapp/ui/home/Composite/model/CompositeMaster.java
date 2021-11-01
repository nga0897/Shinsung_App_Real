package com.example.mmsapp.ui.home.Composite.model;

import com.google.gson.annotations.SerializedName;

public class CompositeMaster {

    @SerializedName("code")
    private String code;

    @SerializedName("start_dt")
    private String startDt;

    @SerializedName("end_dt")
    private String endDt;

    @SerializedName("type")
    private String type;

    @SerializedName("no")
    private String no;

    @SerializedName("name")
    private String name;

    @SerializedName("use_yn")
    private String useYn;

    @SerializedName("pmid")
    private String pmId;

    @SerializedName("staff_tp")
    private String staffTp;

    @SerializedName("het_ca")//End shift
    private String endShift;

    public CompositeMaster(String code, String startDt, String endDt, String type, String no, String name, String useYn, String pmId, String staffTp, String endShift) {
        this.code = code;
        this.startDt = startDt;
        this.endDt = endDt;
        this.type = type;
        this.no = no;
        this.name = name;
        this.useYn = useYn;
        this.pmId = pmId;
        this.staffTp = staffTp;
        this.endShift = endShift;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getPmId() {
        return pmId;
    }

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }

    public String getStaffTp() {
        return staffTp;
    }

    public void setStaffTp(String staffTp) {
        this.staffTp = staffTp;
    }

    public String getEndShift() {
        return endShift;
    }

    public void setEndShift(String endShift) {
        this.endShift = endShift;
    }
}
