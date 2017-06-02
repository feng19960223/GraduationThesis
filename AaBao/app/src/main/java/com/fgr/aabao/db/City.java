package com.fgr.aabao.db;

import org.litepal.crud.DataSupport;

/**
 * 作者：Fgr on 2017/5/5 09:26
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：天气 市
 */

public class City extends DataSupport {
    private int id;
    private String cityName;// 市的名字
    private int cityCode;// 市的代号
    private int provinceId;// 记录当前市所属省的id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

}
