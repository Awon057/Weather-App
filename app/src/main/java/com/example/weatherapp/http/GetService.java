package com.example.weatherapp.http;

import com.example.weatherapp.model.Data;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetService {
    @GET("forecast")
    Call<Data> getData(@Query("lat") Double latitude,
                       @Query("lon") Double longitude,
                       @Query("APPID") String appid);
}
