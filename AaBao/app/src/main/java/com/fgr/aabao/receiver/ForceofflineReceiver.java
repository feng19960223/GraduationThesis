package com.fgr.aabao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.fgr.aabao.R;
import com.fgr.aabao.application.MyApp;
import com.fgr.aabao.ui.MainActivity;

/**
 * 作者：Fgr on 2017/4/30 14:22
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：通过广播，强制下线，实现账号的切换
 * 现在开启的是MainActivity，以后应改为LoginActivity
 */

public class ForceofflineReceiver extends BroadcastReceiver {
    public static final String ACTION_FORCEOFFLINE = "com.fgr.aabao.broadcastbestpractice.FORCE_OFFLINE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.string_warning);
        builder.setMessage(R.string.string_warning_quit);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.string_do, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyApp.finishAll();// 销毁所有活动
                Intent newIntent = new Intent(context, MainActivity.class);
                context.startActivity(newIntent);
            }
        });
        builder.show();
    }
    //    强制下线
    //    Intent intent = new Intent("com.fgr.aabao.broadcastbestpractice.FORCE_OFFLINE");
    //    sendBroadcast(intent);
}
