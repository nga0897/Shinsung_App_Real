package com.example.mmsapp.ui.home.Composite.apiInterface;

import com.example.mmsapp.ui.home.Composite.apiInterface.response.ScanMachineRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AddMoldAPI {

    @GET("ActualWO/MoldMgtData")
    Call<ScanMachineRes> findMold(
            @Query(value = "page",encoded = true) int page,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sort,
            @Query(value = "md_no",encoded = true) String mdNo,
            @Query(value = "md_nm",encoded = true) String mdNm,
            @Query(value = "_search",encoded = true) boolean search
    );

}
