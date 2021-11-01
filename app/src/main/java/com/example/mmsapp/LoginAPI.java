package com.example.mmsapp;

import com.example.mmsapp.checkVerRes.CheckVerRes;
import com.example.mmsapp.service.BaseMessageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginAPI {

    @GET("home/API_Login")
    Call<BaseMessageResponse> login(
            @Query(value = "user",encoded = true) String userName,
            @Query(value = "password",encoded = true) String password,
            @Query(value = "type",encoded = true) String type
    );

    @GET("Home/API_APP_Version")
    Call<CheckVerRes> checkVersion(
            @Query(value = "phienban",encoded = true) int versionCode,
            @Query(value = "name_app",encoded = true) String appName
    );

}
