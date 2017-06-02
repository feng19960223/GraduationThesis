package com.fgr.aabao.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.utils.BmobE;
import com.fgr.aabao.utils.HttpUtils;
import com.fgr.aabao.utils.UIUtils;

import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：Fgr on 2017/5/7 07:14
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：登录
 */

public class LoginActivity extends BaseActivity {

    private EditText et_username;
    private EditText et_password;
    private TextInputLayout til_username;
    private TextInputLayout til_password;
    private CheckBox checkBox;
    private ImageView login_bg;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        til_username = (TextInputLayout) findViewById(R.id.til_username);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        checkBox = (CheckBox) findViewById(R.id.remember_pass);
        til_password.setCounterEnabled(true);
        til_password.setCounterMaxLength(24);
        login_bg = (ImageView) findViewById(R.id.login_bg);
        String bingPic = pref.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(login_bg);
        } else {
            loadBingPic();
        }
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String username = pref.getString("username", "");
            String password = pref.getString("password", "");
            et_username.setText(username);
            et_password.setText(password);
            checkBox.setChecked(true);
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.tv_forget).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 24) {
                    til_password.setError("密码的最大长度是24");
                } else {
                    til_password.setErrorEnabled(false);
                }
            }
        });
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_username.setErrorEnabled(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上导航按钮
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                jumpTo(ForgetActivity.class, false);
                break;
            case R.id.tv_register:
                jumpTo(RegisterActivity.class, false);
                break;
            case R.id.btn_login:
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    til_username.setError("邮箱或账号不能为空！");
                    return;
                }
                if (username.length() < 6) {
                    til_username.setError("账号的最小长度是6！");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    til_password.setError("密码不能为空！");
                    return;
                }
                if (password.length() < 6) {
                    til_password.setError("密码的最小长度是6！");
                    return;
                }
                if (password.length() > 24) {
                    return;
                }
                String passwordReges = "^[A-Za-z0-9]+$";
                if (!password.matches(passwordReges)) {
                    til_password.setError("密码只能有字母和数字组成！");
                    return;
                }
                showProgressDialog();
                BmobUser.loginByAccount(username, password, new LogInListener<MyUser>() {
                    @Override
                    public void done(MyUser user, BmobException e) {
                        closeProgressDialog();
                        if (user != null) {
                            // 记住用户名，不记住密码
                            editor = pref.edit();
                            if (checkBox.isChecked()) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("username", username);
                                editor.putString("password", password);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            jumpTo(MainActivity.class, true);
                        } else {
                            BmobE.E(LoginActivity.this, e);
                        }
                    }
                });
                break;
            default:
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
                        Glide.with(LoginActivity.this).load(bingPic).into(login_bg);
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
