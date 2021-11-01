package com.example.mmsapp.ui.home.ActualWO.api;

import com.example.mmsapp.ui.home.ActualWO.api.response.ActualWOHomeMasterRes;
import com.example.mmsapp.ui.home.ActualWO.api.response.GetDatawActualRes;
import com.example.mmsapp.ui.home.Manufacturing.model.ActualWOdetailMaster;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ActualWOAPI {

    @GET("ActualWO/Getdataw_actual_primary")
    Call<ActualWOHomeMasterRes> getListActualWOHomeMaster(
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sord
    );

    @GET("ActualWO/Getdataw_actual")
    Call<GetDatawActualRes> getListDatawActual(
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sord,
            @Query(value = "at_no",encoded = true) String atNo
    );


    @GET("ActualWO/getdetail_actual")
    Call<List<ActualWOdetailMaster>> getListDetailActual(
            @Query(value = "id",encoded = true) String id,
            @Query(value = "_search",encoded = true) boolean _search,
            @Query(value = "nd",encoded = true) String nd,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sord
    );

    @GET("ActualWO/Getdataw_actual_primary")
    Call<ActualWOHomeMasterRes> searchPo(
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "page",encoded = true) int page,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sord,
            @Query(value = "at_no",encoded = true) String atNo,
            @Query(value = "product",encoded = true) String product,
            @Query(value = "product_name",encoded = true) String productName,
            @Query(value = "model",encoded = true) String model,
            @Query(value = "regstart",encoded = true) String regStart,
            @Query(value = "regend",encoded = true) String regEnd
    );

}
