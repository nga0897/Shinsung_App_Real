package com.example.mmsapp.ui.home.Divide.api;


import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.ui.home.Divide.model.DivDetailMaster;
import com.example.mmsapp.ui.home.Divide.api.response.DeceviceStaRes;
import com.example.mmsapp.ui.home.Divide.api.response.GetMtDateWebRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DivideAPI {

    @GET("ActualWO/getmt_date_web")
    Call<GetMtDateWebRes> getMtDateWeb(
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sort,
            @Query(value = "_search",encoded = true) boolean search,
            @Query(value = "mc_type",encoded = true) String mcType,
            @Query(value = "mc_no",encoded = true) String mcNo,
            @Query(value = "mc_nm",encoded = true) String mcNm
    );

    @GET("ActualWO/Decevice_sta")
    Call<DeceviceStaRes> divideSta(
            @Query(value = "mt_cd",encoded = true) String mtCd,
            @Query(value = "number_dv",encoded = true) String numberDv
    );

    @GET("ActualWO/ds_mapping_sta")
    Call<List<DivDetailMaster>> dsMappingSta(
            @Query(value = "mt_cd",encoded = true) String mtCd,
            @Query(value = "_search",encoded = true) boolean search,
            @Query(value = "nd",encoded = true) String nd,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sort

    );

    @GET("ActualWO/Changebb_dv")
    Call<BaseMessageResponse> changebbDv(
            @Query(value = "bb_no",encoded = true) String bbNo,
            @Query(value = "wmtid",encoded = true) String wmtId

    );

    @GET("ActualWO/change_gr_dv")
    Call<BaseMessageResponse> changeGrDv(
            @Query(value = "value_new",encoded = true) String valueNew,
            @Query(value = "value_old",encoded = true) String valueOld,
            @Query(value = "wmtid",encoded = true) String wmtId
    );

    @GET("ActualWO/destroyDevide")
    Call<BaseMessageResponse> destroyDivide(
            @Query(value = "mt_cd",encoded = true) String mtCd
    );

}
