package com.example.mmsapp.checkVerRes;

import com.google.gson.annotations.SerializedName;

public class DataRes {

    @SerializedName("id_app")
    private int appId;

    @SerializedName("type")
    private String type;

    @SerializedName("name_file")
    private String fileName;

    @SerializedName("version")
    private int version;

    @SerializedName("chg_dt")
    private String chgDt;

    public DataRes(int appId, String type, String fileName, int version, String chgDt) {
        this.appId = appId;
        this.type = type;
        this.fileName = fileName;
        this.version = version;
        this.chgDt = chgDt;
    }


    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getChgDt() {
        return chgDt;
    }

    public void setChgDt(String chgDt) {
        this.chgDt = chgDt;
    }
}
