package com.example.mmsapp.ui.home.Manufacturing.model;

import com.google.gson.annotations.SerializedName;

public class ActualWOdetailMaster {

    @SerializedName("mt_cd")
    private String mt_cd;

    @SerializedName("bb_no")
    private String bb_no;

    @SerializedName("gr_qty")
    private int gr_qty;

    @SerializedName("sl_tru_ng")
    private int count_table2;

    private int no;

    public ActualWOdetailMaster(String mt_cd, String bb_no, int gr_qty, int count_table2, int no) {
        this.mt_cd = mt_cd;
        this.bb_no = bb_no;
        this.gr_qty = gr_qty;
        this.count_table2 = count_table2;
        this.no = no;

    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMt_cd() {
        return mt_cd;
    }

    public void setMt_cd(String mt_cd) {
        this.mt_cd = mt_cd;
    }

    public String getBb_no() {
        return bb_no;
    }

    public void setBb_no(String bb_no) {
        this.bb_no = bb_no;
    }

    public int getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(int gr_qty) {
        this.gr_qty = gr_qty;
    }

    public int getCount_table2() {
        return count_table2;
    }

    public void setCount_table2(int count_table2) {
        this.count_table2 = count_table2;
    }
}
