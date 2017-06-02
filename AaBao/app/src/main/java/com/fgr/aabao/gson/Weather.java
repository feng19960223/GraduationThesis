package com.fgr.aabao.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：Fgr on 2017/5/5 11:54
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
