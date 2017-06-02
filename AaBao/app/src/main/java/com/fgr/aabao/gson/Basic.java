package com.fgr.aabao.gson;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 作者：Fgr on 2017/5/5 11:46
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
