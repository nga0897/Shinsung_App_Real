package com.example.mmsapp.ui.status.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StatusAPI {

    @GET("Tims/Get_Status_Bobin")
    Call<GetStatusBobbinRes> getStatusBobbin(
            @Query(value = "bb_no",encoded = true) String bbNo
    );

    @GET("DevManagement/Detailcontainer_composite")
    Call<DetailContainerCompositeRes> detailContainerComposite(
            @Query(value ="mt_no") String mtNo
    );
}
