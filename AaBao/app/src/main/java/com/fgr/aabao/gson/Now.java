package com.fgr.aabao.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：Fgr on 2017/5/5 11:48
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
