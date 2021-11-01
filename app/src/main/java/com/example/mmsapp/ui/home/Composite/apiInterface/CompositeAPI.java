package com.example.mmsapp.ui.home.Composite.apiInterface;

import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.ui.home.Composite.model.PositionOfController;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ConfirmAddWorkerRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.DestroyOldResultRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.InfoMcWkMoldRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ListWorkerRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ModifyProcessMachineRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ModifyProcessMachineWKRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CompositeAPI {

    @GET("ActualWO/search_staff_pp")
    Call<ListWorkerRes> getListWorker(
        @Query(value = "page",encoded = true) int page,
        @Query(value = "rows",encoded = true) int rows,
        @Query(value = "sidx",encoded = true) String sidx,
        @Query(value = "sord",encoded = true) String sort,
        @Query(value = "md_no",encoded = true) String mdNo,
        @Query(value = "md_nm",encoded = true) String mdNm,
        @Query(value = "_search",encoded = true) boolean search
    );

    @GET("ActualWO/search_staff_pp") //Note: This function is difference with above
    Call<ListWorkerRes> scanWorker(
        @Query(value="page",encoded = true) int page,
        @Query(value = "rows",encoded = true) int rows,
        @Query(value = "sidx",encoded = true) String sidx,
        @Query(value = "sord",encoded = true) String sort,
        @Query(value = "userid",encoded = true) String userId,
        @Query(value = "md_nm",encoded = true) String mdNm,
        @Query(value = "_search",encoded = true) boolean search
    );

    @GET("ActualWO/get_staff")
    Call<List<PositionOfController>> getPositionOfController(); //Function be commented

    @GET("ActualWO/Createprocess_unitstaff")
    Call<BaseMessageResponse> confirmAddWorker(
            @Query(value = "staff_id",encoded = true) String staffID,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "use_yn",encoded = true) String useYn
    );

    @GET("ActualWO/Createprocessstaff_duplicate")
    Call<DestroyOldResultRes> destroyOldResult(
            @Query(value = "staff_tp",encoded = true) String staffTp,
            @Query(value = "staff_id",encoded = true) String staffID,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "use_yn",encoded = true) String useYn,
            @Query(value = "id_update",encoded = true) String idUpdate,
            @Query(value = "start",encoded = true) String start,
            @Query(value = "end",encoded = true) String end,
            @Query(value = "remark",encoded = true) String remark
    );

    @GET("ActualWO/Getinfo_mc_wk_mold")
    Call<InfoMcWkMoldRes> getCompositeMaster(
            @Query(value = "id_actual",encoded = true) String actualId
    );


    @GET("ActualWO/DeleteMold_mc_wk_actual")
    Call<BaseMessageResponse> deleteMoldMcWkActual(
            @Query(value = "id",encoded = true) String id,
            @Query(value = "sts",encoded = true) String sts
    );

    @GET("ActualWO/Modifyprocess_unitstaff")
    Call<ModifyProcessMachineWKRes> modifyProcessMachineWK(
            @Query(value = "staff_id",encoded = true) String staffId,
            @Query(value = "staff_tp",encoded = true) String staffTp,
            @Query(value = "psid",encoded = true) String psId,
            @Query(value = "use_yn",encoded = true) String useYn,
            @Query(value = "start",encoded = true) String start,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "end",encoded = true) String end,
            @Query(value = "remark",encoded = true) String remark
    );

    @GET("ActualWO/Modifyprocessmachine_unit")
    Call<ModifyProcessMachineRes> modifyProcessMachine(
            @Query(value = "mc_no",encoded = true) String mcNo,
            @Query(value = "pmid",encoded = true) String pmId,
            @Query(value = "use_yn",encoded = true) String useYn,
            @Query(value = "start",encoded = true) String start,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "end",encoded = true) String end,
            @Query(value = "remark",encoded = true) String remark
    );






}
