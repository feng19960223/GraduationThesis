package com.fgr.aabao.db;

import org.litepal.crud.DataSupport;

/**
 * 作者：Fgr on 2017/5/5 09:29
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：天气 县
 */

public class County extends DataSupport {
    private int id;
    private String countyName;// 县的名字
    private String weatherId;//  记录县所对应的天气id
    private int cityId;// 记录当前所属市的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
