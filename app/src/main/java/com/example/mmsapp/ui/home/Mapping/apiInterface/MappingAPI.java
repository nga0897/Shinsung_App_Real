package com.example.mmsapp.ui.home.Mapping.apiInterface;

import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.ui.home.Mapping.model.MappingDetailMaster;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.CancelMappingRes;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.CheckUpdateGrtyRes;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.GetMtDateWebRes;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.InsertwMaterialRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MappingAPI {

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

    @GET("ActualWO/check_update_grty")
    Call<CheckUpdateGrtyRes> checkUpdateGrty(
            @Query(value = "mt_cd",encoded = true) String mtCd,
            @Query(value = "value",encoded = true) String value,
            @Query(value = "wmtid",encoded = true) int wmtId
    );

    @GET("ActualWO/insertw_material")
    Call<InsertwMaterialRes> insertwMaterial(
            @Query(value = "style_no",encoded = true) String styleNo,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "ROT",encoded = true) String rot,
            @Query(value = "bb_no",encoded = true) String bbNo
    );

    @GET("ActualWO/ds_mapping_w")
    Call<List<MappingDetailMaster>> dsMappingW(
            @Query(value = "mt_cd",encoded = true) String mtCd
    );

    @GET("ActualWO/Finish_back")
    Call<CancelMappingRes> finishBack(
            @Query(value = "wmmid",encoded = true) int wmmId
    );

    @GET("ActualWO/Cancel_mapping")
    Call<CancelMappingRes> cancelMapping(
            @Query(value = "wmmid",encoded = true) int wmmId
    );

    @GET("ActualWO/savereturn_lot")
    Call<CancelMappingRes> saveReturnLot(
            @Query(value = "soluong",encoded = true) String quantity,
            @Query(value = "mt_cd") String mtCd,
            @Query(value = "mt_lot",encoded = true) String mtLot
    );

    @GET("ActualWO/Xoa_mt_pp_composite")
    Call<BaseMessageResponse> deleteMtPpComposite(
            @Query(value = "id",encoded = true) int id
    );

    @GET("ActualWO/insertw_material_mping")
    Call<BaseMessageResponse> insertWMaterialMapping(
            @Query(value = "mt_cd",encoded = true) String mtCd,
            @Query(value = "mt_mapping") String mtMapping,
            @Query(value = "id_actual",encoded = true) String idActual,
            @Query(value = "bb_no",encoded = true) String bbNo
    );

    @GET("ActualWO/updateDescriptionWMaterialInfo")
    Call<BaseMessageResponse> updateDescriptionWMaterialInfo(
            @Query(value = "wmtid",encoded = true) int wmtid,
            @Query(value = "description",encoded = true) String description
    );

}
