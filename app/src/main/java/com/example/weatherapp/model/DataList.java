package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class DataList {
    @SerializedName("dt")
    long time;
    @SerializedName("main")
    Temp temp;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }
}
