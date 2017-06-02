package com.fgr.aabao.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 作者：Fgr on 2017/5/5 09:38
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：网络服务
 */

public class HttpUtils {
    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
