package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Temp {
    @SerializedName("temp")
    Double temp;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
