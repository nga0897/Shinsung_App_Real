package com.example.mmsapp.ui.home.Composite.apiInterface;

import com.example.mmsapp.ui.home.Composite.apiInterface.response.CreateProcessMachineRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.DestroyPreviousResultRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ListWorkerRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ScanMachineRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MachineAPI {

    @GET("ActualWO/searchpopupmachine")
    Call<ScanMachineRes> scanMachine(
            @Query(value = "page",encoded = true) int page,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sort,
            @Query(value = "mc_no",encoded = true) String mcNo,
            @Query(value = "md_nm",encoded = true) String mdNm,
            @Query(value = "_search",encoded = true) boolean search
    );

    @GET("ActualWO/searchpopupmachine") // Difference with above
    Call<ScanMachineRes> getAllMachine(
            @Query(value = "page",encoded = true) int page,
            @Query(value = "rows",encoded = true) int rows,
            @Query(value = "sidx",encoded = true) String sidx,
            @Query(value = "sord",encoded = true) String sort,
            @Query(value = "md_no",encoded = true) String mdNo,
            @Query(value = "md_nm",encoded = true) String mdNm,
            @Query(value = "_search",encoded = true) boolean search
    );


    @GET("ActualWO/Createprocessmachine_unit")
    Call<CreateProcessMachineRes> createProcessMachineUnit(
            @Query(value = "mc_no",encoded = true) String mcNo,
            @Query(value = "use_yn",encoded = true) String useYn,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "remark",encoded = true) String remark
    );

    @GET("ActualWO/Createprocessmachine_duplicate")
    Call<DestroyPreviousResultRes> destroyPreviousResult(
            @Query(value = "mc_no",encoded = true) String mcNo,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "use_yn",encoded = true) String useYn,
            @Query(value = "id_update",encoded = true) String idUpdate,
            @Query(value = "start",encoded = true) String start,
            @Query(value = "end",encoded = true) String end,
            @Query(value = "remark",encoded = true) String remark
    );

}
