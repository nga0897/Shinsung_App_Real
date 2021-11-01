package com.example.mmsapp.ui.home.Composite.model;

import com.google.gson.annotations.SerializedName;

public class ItemStaffMaster {

    @SerializedName("position_cd")
    private String positionCd;

    @SerializedName("userid")
    private String userId;

    @SerializedName("RowNum")
    private String rowNum;

    @SerializedName("uname")
    private String uName;

    @SerializedName("nick_name")
    private String nickName;

    @SerializedName("mc_no")
    private String mcNo;

    public ItemStaffMaster(String positionCd, String userId, String rowNum, String uName, String nickName, String mcNo) {
        this.positionCd = positionCd;
        this.userId = userId;
        this.rowNum = rowNum;
        this.uName = uName;
        this.nickName = nickName;
        this.mcNo = mcNo;
    }

    public String getPositionCd() {
        return positionCd;
    }

    public void setPositionCd(String positionCd) {
        this.positionCd = positionCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMcNo() {
        return mcNo;
    }

    public void setMcNo(String mcNo) {
        this.mcNo = mcNo;
    }
}
