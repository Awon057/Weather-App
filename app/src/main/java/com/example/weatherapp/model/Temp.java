package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Temp {
    @SerializedName("temp")
    Double dayTemp;

    public Double getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(Double dayTemp) {
        this.dayTemp = dayTemp;
    }
}
