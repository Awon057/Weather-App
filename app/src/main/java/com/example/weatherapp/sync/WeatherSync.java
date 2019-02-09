package com.example.weatherapp.sync;

import android.provider.Contacts;

import com.example.weatherapp.http.GetService;
import com.example.weatherapp.model.Data;
import com.example.weatherapp.util.RetrofitClient;
import com.example.weatherapp.util.ReturnData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherSync {
    private static GetService apiInterface;

    public static void getData(Double latitude, Double longitude, final ReturnData returnData){
        RetrofitClient api = new RetrofitClient();
        apiInterface = api.getApiClient().create(GetService.class);

        String appId = "b7c6524ab9081a7fd3bad37c6927afe9";
        Call<Data> call = apiInterface.getData(latitude,longitude,appId);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    System.out.println("lat: ");
                    returnData.returnData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                call.cancel();
                System.out.println("Failed: ");
            }
        });
    }
}

