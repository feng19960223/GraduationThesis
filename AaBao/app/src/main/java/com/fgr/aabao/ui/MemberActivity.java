package com.fgr.aabao.ui;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.fgr.aabao.R;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.utils.BmobE;

import java.util.Calendar;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/8 09:57
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：成员增加，修改，查看
 */

public class MemberActivity extends BaseActivity {
    private String from;
    private TextInputLayout til_name,til_phone;
    private EditText et_name, et_phone, et_company, et_post, et_address, et_dear, et_birthday, et_other, et_remark;
    private Member intent_member;
    private Button btn_member_save;
    private String intent_member_objectId;

    @Override
    protected void init() {
        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        til_phone.setCounterEnabled(true);
        til_phone.setCounterMaxLength(11);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_company = (EditText) findViewById(R.id.et_company);
        et_post = (EditText) findViewById(R.id.et_post);
        et_address = (EditText) findViewById(R.id.et_address);
        et_dear = (EditText) findViewById(R.id.et_dear);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_other = (EditText) findViewById(R.id.et_other);
        et_remark = (EditText) findViewById(R.id.et_remark);
        btn_member_save = (Button) findViewById(R.id.btn_member_save);
        from = getIntent().getStringExtra("member_from");
        intent_member = getIntent().getParcelableExtra("member_data");
        intent_member_objectId = getIntent().getStringExtra("intent_member_objectId");
        if ("add".equals(from)) {
            actionBar.setTitle(R.string.string_add_member);// 成员增加
        } else if ("look".equals(from)) {//查看
            actionBar.setTitle(R.string.string_look);// 信息查看
            btn_member_save.setVisibility(View.GONE);
            showMember(intent_member, false);
        } else if ("alter".equals(from)) {// 修改
            actionBar.setTitle(R.string.string_alter_member);// 信息修改
            showMember(intent_member, true);
        }
    }

    @Override
    protected void initListener() {
        et_birthday.setFocusable(false);
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {
                        et_birthday.setText(year + "/" + (monthofYear + 1) + "/" + dayofMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        btn_member_save.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_name.setErrorEnabled(false);
            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_phone.setErrorEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_member_save:
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    til_name.setError("名字不能为空！");
                }
                if(et_phone.getText().toString().trim().length()>11){
                    til_phone.setError("电话最大长度为11位");
                }
                if ("add".equals(from)) {
                    addMember();
                } else if ("alter".equals(from)) {
                    alterMember();
                }
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

    private void addMember() {
        // 注意：不能调用gameScore.setObjectId("")方法
        getMember().save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    showAlertDialog("创建数据成功！");
                    et_name.setText("");
                    et_phone.setText("");
                    et_company.setText("");
                    et_post.setText("");
                    et_address.setText("");
                    et_dear.setText("");
                    et_birthday.setText("");
                    et_other.setText("");
                    et_remark.setText("");
                } else {
                    BmobE.E(MemberActivity.this, e);
                }
            }
        });
    }

    private void alterMember() {
        getMember().update(intent_member_objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showAlertDialog("更新成功");
                } else {
                    BmobE.E(MemberActivity.this, e);
                }
            }
        });
    }

    private Member getMember() {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String company = et_company.getText().toString().trim();
        String post = et_post.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String dear = et_dear.getText().toString().trim();
        String birthday = et_birthday.getText().toString().trim();
        String other = et_other.getText().toString().trim();
        String remark = et_remark.getText().toString().trim();
        return new Member(name, phone, company, post, address, dear, birthday, other, remark);
    }

    /**
     * 传入false，EditText不可被编辑
     */
    private void showMember(Member member, boolean enabled) {
        et_name.setText(member.getName());
        et_phone.setText(member.getPhone());
        et_company.setText(member.getCompany());
        et_post.setText(member.getPost());
        et_address.setText(member.getAddress());
        et_dear.setText(member.getDear());
        et_birthday.setText(member.getBirthday());
        et_other.setText(member.getOther());
        et_remark.setText(member.getRemark());
        if (!enabled) {
            et_name.setEnabled(false);
            et_phone.setEnabled(false);
            et_company.setEnabled(false);
            et_post.setEnabled(false);
            et_address.setEnabled(false);
            et_dear.setEnabled(false);
            et_birthday.setEnabled(false);
            et_other.setEnabled(false);
            et_remark.setEnabled(false);
            et_name.setFocusable(false);
            et_phone.setFocusable(false);
            et_company.setFocusable(false);
            et_post.setFocusable(false);
            et_address.setFocusable(false);
            et_dear.setFocusable(false);
            et_birthday.setFocusable(false);
            et_other.setFocusable(false);
            et_remark.setFocusable(false);
        }
    }
}
