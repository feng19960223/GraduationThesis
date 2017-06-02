package com.fgr.aabao.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.utils.BmobE;
import com.fgr.aabao.utils.HttpUtils;

import java.io.IOException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：Fgr on 2017/5/7 14:28
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：注册
 */

public class RegisterActivity extends BaseActivity {
    private EditText et_register_mail;
    private EditText et_register_username;
    private EditText et_register_password;
    private TextInputLayout til_register_mail;
    private TextInputLayout til_register_username;
    private TextInputLayout til_register_password;
    private ImageView register_bg;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void init() {
        actionBar.setTitle(R.string.string_register);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        et_register_mail = (EditText) findViewById(R.id.et_register_mail);
        et_register_username = (EditText) findViewById(R.id.et_register_username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        til_register_mail = (TextInputLayout) findViewById(R.id.til_register_mail);
        til_register_username = (TextInputLayout) findViewById(R.id.til_register_username);
        til_register_username.setCounterEnabled(true);
        til_register_username.setCounterMaxLength(12);
        til_register_password = (TextInputLayout) findViewById(R.id.til_register_password);
        til_register_password.setCounterEnabled(true);
        til_register_password.setCounterMaxLength(24);
        register_bg = (ImageView) findViewById(R.id.register_bg);
        String bingPic = pref.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(register_bg);
        } else {
            loadBingPic();
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.btn_register).setOnClickListener(this);// 注册
        findViewById(R.id.tv_have).setOnClickListener(this);// 已有账号
        findViewById(R.id.tv_treaty).setOnClickListener(this);// 协议
        et_register_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 24) {
                    til_register_password.setError("账号的最大长度是24");
                } else {
                    til_register_password.setErrorEnabled(false);
                }
            }
        });
        et_register_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 12) {
                    til_register_username.setError("账号的最大长度是12");
                } else {
                    til_register_username.setErrorEnabled(false);
                }
            }
        });
        et_register_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_register_mail.setErrorEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                String email = et_register_mail.getText().toString().trim();
                String username = et_register_username.getText().toString().trim();
                String password = et_register_password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    til_register_mail.setError("邮箱不能为空!");
                    return;
                }
                String emailRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
                if (!email.matches(emailRegex)) {
                    til_register_mail.setError("不正确的的邮箱!");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    til_register_username.setError("账号不能为空!");
                    return;
                }
                if (username.length() < 6) {
                    til_register_username.setError("账号的最小长度是6!");
                    return;
                }
                if (username.length() > 12) {
                    til_register_username.setError("账号的最大长度是12!");
                    return;
                }
                String usernameRegex = "^\\w+$";
                if (!username.matches(usernameRegex)) {
                    til_register_username.setError("账号不能包含特殊字符!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    til_register_password.setError("密码不能为空!");
                    return;
                }
                if (password.length() < 6) {
                    til_register_password.setError("密码的最小长度是6!");
                    return;
                }
                if (password.length() > 24) {
                    return;
                }
                String passwordReges = "^[A-Za-z0-9]+$";
                if (!password.matches(passwordReges)) {
                    til_register_password.setError("密码只能有字母和数字组成!");
                    return;
                }
                MyUser myUser = new MyUser();
                myUser.setUsername(username);
                myUser.setPassword(password);
                myUser.setEmail(email);
                showProgressDialog();
                myUser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser s, BmobException e) {
                        closeProgressDialog();
                        if (e == null) {
                            showAlertDialog("注册成功");
                        } else {
                            BmobE.E(RegisterActivity.this, e);
                        }
                    }
                });
                break;
            case R.id.tv_treaty:
                jumpTo(TreatyActivity.class, false);
                break;
            case R.id.tv_have:
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上返回按钮
                finish();
                break;
            default:
        }
        return true;
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
                        Glide.with(RegisterActivity.this).load(bingPic).into(register_bg);
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
