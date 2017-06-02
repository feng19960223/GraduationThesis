package com.fgr.aabao.gson;

/**
 * 作者：Fgr on 2017/5/5 11:45
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
