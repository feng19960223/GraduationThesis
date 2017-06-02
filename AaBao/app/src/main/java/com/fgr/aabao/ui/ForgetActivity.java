package com.fgr.aabao.ui;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fgr.aabao.R;
import com.fgr.aabao.utils.BmobE;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/7 13:06
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：忘记密码，通过找回密码
 */

public class ForgetActivity extends BaseActivity {
    private TextInputLayout til_forget;
    private EditText et_forget;

    @Override
    protected void init() {
        actionBar.setTitle(R.string.string_forget_seek);
        et_forget = (EditText) findViewById(R.id.et_forget);
        til_forget = (TextInputLayout) findViewById(R.id.til_forget);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.btn_forget_do).setOnClickListener(this);
        et_forget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_forget.setErrorEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forget_do:
                final String email = et_forget.getText().toString().trim();
                String emailRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
                if (!email.matches(emailRegex)) {
                    til_forget.setError("请输入正确的邮箱！");
                    return;
                }
                showProgressDialog();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        closeProgressDialog();
                        if (e == null) {
                            showAlertDialog("重置密码请求成功，我们已向您的邮箱" + email + "发送了一份邮件，请到邮箱进行密码重置操作。");
                        } else {
                            BmobE.E(ForgetActivity.this, e);
                        }
                    }
                });
                break;
            default:
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
}
