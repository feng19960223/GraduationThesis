package com.fgr.aabao.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：Fgr on 2017/5/11 14:56
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class MyUser extends BmobUser {
    private String dear;// 昵称
    private BmobFile icon;// 头像

    public String getDear() {
        return dear;
    }

    public void setDear(String dear) {
        this.dear = dear;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }
}
