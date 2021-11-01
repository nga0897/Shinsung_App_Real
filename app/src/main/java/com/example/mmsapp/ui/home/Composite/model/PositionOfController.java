package com.example.mmsapp.ui.home.Composite.model;

import com.google.gson.annotations.SerializedName;

public class PositionOfController {
    @SerializedName("dt_cd")
    private String dtCd;

    @SerializedName("dt_nm")
    private String dtNm;

    public PositionOfController(String dtCd, String dtNm) {
        this.dtCd = dtCd;
        this.dtNm = dtNm;
    }

    public String getDtCd() {
        return dtCd;
    }

    public void setDtCd(String dtCd) {
        this.dtCd = dtCd;
    }

    public String getDtNm() {
        return dtNm;
    }

    public void setDtNm(String dtNm) {
        this.dtNm = dtNm;
    }
}
