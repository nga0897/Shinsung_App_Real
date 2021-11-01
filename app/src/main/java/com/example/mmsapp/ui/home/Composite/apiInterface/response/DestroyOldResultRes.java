package com.example.mmsapp.ui.home.Composite.apiInterface.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestroyOldResultRes {
    @SerializedName("psid")
    private int psId;

    @SerializedName("staff_id")
    private String staffId;

    @SerializedName("uname")
    private String uName;

    @SerializedName("start_dt")
    private String startDt;

    @SerializedName("end_dt")
    private String endDt;

    @SerializedName("use_yn")
    private String useYn;



}
