package com.fgr.aabao.db;

import org.litepal.crud.DataSupport;

/**
 * 作者：Fgr on 2017/5/5 09:21
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：天气 省
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;// 省的名字
    private int provinceCode;// 省的代号

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

}
