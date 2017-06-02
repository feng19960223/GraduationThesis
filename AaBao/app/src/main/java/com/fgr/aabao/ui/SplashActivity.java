package com.fgr.aabao.ui;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.fgr.aabao.R;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.permission.PermissionListener;
import com.fgr.aabao.permission.PermissionManager;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

/**
 * 作者：Fgr on 2017/5/13 10:40
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：闪屏页面，广告
 */

public class SplashActivity extends BaseActivity {
    private boolean canJump;// 用于判断是否可以跳过广告，进入页面
    PermissionManager helper;
    private static final int REQUEST_CODE = 1;
    private LinearLayout container;

    @Override
    protected void init() {
        container = (LinearLayout) findViewById(R.id.container);
        helper = PermissionManager.with(this)
                //添加权限请求码
                .addRequestCode(SplashActivity.REQUEST_CODE)
                //设置权限，可以添加多个权限
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                        requestAds();
                    }

                    @Override
                    public void onDenied() {
                        //用户拒绝该权限时调用
                        finish();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        helper.setIsPositive(true);
                        helper.request();
                    }
                })
                //请求权限
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                helper.onPermissionResult(permissions, grantResults);
                break;
        }
    }

    @Override
    protected void initListener() {}

    @Override
    public void onClick(View view) {}

    // 加载开屏广告
    private void requestAds() {
        String appId = "1105685737";
        String adId = "2090328236167899";
        new SplashAD(this, container, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {//广告显示完毕
                forward();
            }

            @Override
            public void onNoAD(int i) {//广告加载失败
                forward();
            }

            @Override
            public void onADPresent() {//广告加载成功
            }

            @Override
            public void onADClicked() {//广告被点击
            }

            @Override
            public void onADTick(long l) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            forward();
        }
        canJump = true;
    }

    private void forward() {
        if (canJump) {
            if (MyUser.getCurrentUser(MyUser.class) != null) {
                jumpTo(MainActivity.class, true);
            } else {
                jumpTo(LoginActivity.class, true);
            }
        } else {
            canJump = true;
        }
    }
}
