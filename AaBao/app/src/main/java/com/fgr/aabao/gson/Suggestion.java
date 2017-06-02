package com.fgr.aabao.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：Fgr on 2017/5/5 11:50
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }

}
