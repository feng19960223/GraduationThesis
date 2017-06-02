package com.fgr.aabao.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fgr.aabao.R;
import com.fgr.aabao.application.MyApp;
import com.fgr.aabao.receiver.ForceofflineReceiver;
import com.fgr.aabao.utils.UIUtils;

import java.util.Locale;

/**
 * 作者：Fgr on 2017/4/30 08:33
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：出现了一个小bug，已解决，不显示界面内容，onCreate要用一个参数，两个参数的是不会显示界面的
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ForceofflineReceiver receiver = null;
    private static BaseActivity mForegroundActivity = null;
    protected ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {//  ***这里要用一个参数，两个参数是不会显示页面内容的***
        super.onCreate(savedInstanceState);
        // 尝试调用setContentView(layoutId)方法
        // 尝试根据类名(例如：MainActivity)--->资源文件的名字(activity_main)
        String clazzName = this.getClass().getSimpleName();// MainAcitivity
        if (clazzName.contains("Activity")) {
            String activityName = clazzName.substring(0,
                    clazzName.indexOf("Activity")).toLowerCase(Locale.US);// main
            String resName = "activity_" + activityName;// activity_main
            // 根据resName找到其对应的resId(根据activity_main--->R.layout.activity_main)
            int resId = getResources().getIdentifier(resName, "layout",
                    getPackageName());
            if (resId != 0) {
                // 确实找到了资源ID(R.layout.activity_main)
                setContentView(resId);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);// 得到Toolbar实例
        setSupportActionBar(toolbar);// 将Toolbar的实例传入
        // 得到ActionBar实例
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 让导航按钮显示出来
        }
        init();
        initListener();
        MyApp.addActivity(this);// 将正在创建的活动添加到活动管理器里
    }


    protected abstract void init();

    protected abstract void initListener();

    @Override
    protected void onResume() {
        super.onResume();
        this.mForegroundActivity = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ForceofflineReceiver.ACTION_FORCEOFFLINE);
        receiver = new ForceofflineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);// 将一个要销毁的活动从活动管理器里移除
    }

    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    // 界面跳转的相关方法
    protected void jumpTo(Class<?> clazz, boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    protected void jumpTo(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    private ProgressDialog progressDialog;

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.string_warning);
            progressDialog.setMessage(UIUtils.getString(R.string.string_city_ing));
        }
        progressDialog.show();
    }

    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private AlertDialog.Builder alertDialog;

    protected void showAlertDialog(String message) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.string_warning);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(R.string.string_do, null);
        }
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}
