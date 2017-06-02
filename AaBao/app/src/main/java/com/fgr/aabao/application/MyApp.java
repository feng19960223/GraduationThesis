package com.fgr.aabao.application;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * 作者：Fgr on 2017/4/30 07:51
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：获取到常用的工具类
 */

public class MyApp extends Application {
    private static MyApp mContext;// 获取到主线程的上下文
    private static Handler mMainThreadHandler;// 获取到主线程的Handler
    private static Thread mMainThread;// 获取到主线程
    private static int mMainThreadId;// 获取到主线程Id
    public static List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadId = android.os.Process.myTid();
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);// 发送key给服务 校验key是否正确 百度
        LitePalApplication.initialize(this);// 初始化litepal数据库 Litepal
        Bmob.initialize(this, "8a481161556fc23c5a7ebba353d06a1c", "aabao"); // Bmob
    }

    // 利用广播实现退出，和强制下线功能
    public static void addActivity(Activity activity) {// 向List中添加一个活动
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {// 从List中移除活动
        activities.remove(activity);
    }

    public static void finishAll() {// 将List中存储的活动全部销毁掉
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static MyApp getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }
}
