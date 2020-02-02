package com.company;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherData {

    @SerializedName("main")
    WeatherMain MainObject;

    public WeatherData(WeatherMain weatherMain) {
        this.MainObject = weatherMain;
    }

}