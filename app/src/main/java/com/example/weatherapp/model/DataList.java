package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class DataList {
    @SerializedName("dt_txt")
    String time;
    @SerializedName("main")
    Temp temp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }
}
