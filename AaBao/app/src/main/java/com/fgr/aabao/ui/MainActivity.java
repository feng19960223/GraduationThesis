package com.fgr.aabao.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.adapter.MyMainFragmentPagerAdapter;
import com.fgr.aabao.application.MyApp;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.utils.HttpUtils;
import com.fgr.aabao.utils.UIUtils;

import java.io.IOException;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：Fgr on 2017/4/29 12:34
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：4.29，完成了侧滑菜单，Toolbar菜单，退出应用时的逻辑
 * 4.30, 完成了导航栏，Fragment工厂框架，强制退出，调试一个小bug，不能加载页面BaseActivity有解释
 */

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    MyMainFragmentPagerAdapter adapter;
    private CircleImageView icon;
    private TextView mail;
    private TextView name;
    private ImageView header_bg;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MyUser myUser;

    @Override
    public void init() {
        actionBar.setDisplayShowTitleEnabled(false);// 不显示项目名字
        // 实际上，Toolbar最左侧的按钮就叫做HomeAsUp按钮，它默认的图标是一个返回的箭头，含义是返回上一个活动
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home);// 设置一个导航按钮图
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_activity_main);// 得到DrawerLayout实例
        mNavigationView = (NavigationView) findViewById(R.id.navigationview_activity_main);// 得到NavigationView实例
        mNavigationView.setCheckedItem(R.id.menu_member_manage);// 将会员管理菜单项设置为默认选中
        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_main);// 得到ViewPager实例
        View heardView = mNavigationView.getHeaderView(0);
        icon = (CircleImageView) heardView.findViewById(R.id.circleimageview_nav_header_icon);
        mail = (TextView) heardView.findViewById(R.id.textview_nav_header_mail);
        name = (TextView) heardView.findViewById(R.id.textview_nav_header_username);
        header_bg = (ImageView) heardView.findViewById(R.id.header_bg);
        adapter = new MyMainFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        myUser = BmobUser.getCurrentUser(MyUser.class);
        if (myUser == null) {// 用户不存在，或过期
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.string_warning);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(R.string.string_login_again, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    jumpTo(LoginActivity.class, true);
                }
            });
            alertDialog.show();
        } else {
            infoResume();
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = pref.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(header_bg);
        } else {
            loadBingPic();
        }
    }

    @Override
    public void initListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {// 设置一个菜单项选中事件的监听器
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_member_manage:// 侧滑‘会员管理’菜单按钮
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_activity_manage:// 侧滑‘活动管理’菜单按钮
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_bill_manage:// 侧滑‘账单管理’菜单按钮
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_map:// 侧滑‘地图’菜单按钮
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.menu_weather:// 侧滑‘天气’菜单按钮
                        mViewPager.setCurrentItem(4);
                        break;
                    case R.id.menu_quit:// 侧滑‘退出’菜单按钮
                        MyApp.finishAll();
                        break;
                    case R.id.menu_set:// 侧滑‘设置’菜单按钮
                        jumpTo(SetActivity.class, false);
                        break;
                    default:
                }
                mDrawerLayout.closeDrawers();// 将滑动菜单关闭
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上导航按钮
                mDrawerLayout.openDrawer(GravityCompat.START);// 打开侧滑菜单
                break;
            case R.id.toolbar_map:// 右上‘地图’菜单按钮
                mViewPager.setCurrentItem(5);
                break;
            case R.id.toolbar_weather:// 右上‘天气’菜单按钮
                mViewPager.setCurrentItem(6);
                break;
            case R.id.toolbar_about:// 右上‘关于’菜单按钮
                jumpTo(AboutActivity.class, false);
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);// 加载菜单文件，在onOptionsItemSelected（）方法中处理各个按钮的点击事件
        return true;
    }

    private long lastClickTime = 0;

    @Override
    public void onBackPressed() {// 处理误操作时的退出
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {// 如果侧滑菜单处于打开状态
            mDrawerLayout.closeDrawers();// 关闭
        } else {
            if (lastClickTime <= 0) {
                lastClickTime = System.currentTimeMillis();
                // 使用Snackbar，有一个可交互按鈕，提示时间1500毫秒
                SnackbarShow(UIUtils.getString(R.string.string_again_quit), 1500, UIUtils.getString(R.string.string_do));
            } else {
                long currentClickTime = System.currentTimeMillis();
                if (currentClickTime - lastClickTime < 1500) {
                    finish();
                } else {
                    lastClickTime = System.currentTimeMillis();
                    SnackbarShow(UIUtils.getString(R.string.string_again_quit), 1500, UIUtils.getString(R.string.string_do));
                }
            }
        }
    }

    public void SnackbarShow(String content, int time, String text) {
        Snackbar.make(mDrawerLayout, content, time)
                .setAction(text, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        infoResume();
    }

    private void infoResume() {
        if (myUser.getIcon() != null) {
            Glide.with(this).load(MyUser.getCurrentUser(MyUser.class).getIcon().getUrl()).into(icon);
        }
        if (myUser.getDear() != null) {
            name.setText(MyUser.getCurrentUser(MyUser.class).getDear());
        }
        if (myUser.getEmail() != null) {
            mail.setText(MyUser.getCurrentUser(MyUser.class).getEmail());
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                editor = pref.edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPic).into(header_bg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
