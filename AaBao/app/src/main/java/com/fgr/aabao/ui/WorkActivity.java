package com.fgr.aabao.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fgr.aabao.R;
import com.fgr.aabao.adapter.PersonAdapter;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.bean.Work;
import com.fgr.aabao.inter.OnMyItemClickListener;
import com.fgr.aabao.utils.BmobE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/9 15:27
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：事务增加，修改，查看
 */
public class WorkActivity extends BaseActivity {
    private String from;
    private Work intent_work;
    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private ArrayList<Member> members = new ArrayList<>();
    private Button btn_work_save;
    private TextView tv_info;
    private TextInputLayout til_work, til_address, til_time;
    private EditText et_work, et_address, et_time, et_remark;
    private String intent_work_objectId;

    @Override
    protected void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_activity_work);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//增加删除动画
        til_work = (TextInputLayout) findViewById(R.id.til_work);
        til_address = (TextInputLayout) findViewById(R.id.til_address);
        til_time = (TextInputLayout) findViewById(R.id.til_time);
        et_work = (EditText) findViewById(R.id.et_work);
        et_address = (EditText) findViewById(R.id.et_address);
        et_time = (EditText) findViewById(R.id.et_time);
        et_remark = (EditText) findViewById(R.id.et_remark);
        tv_info = (TextView) findViewById(R.id.tv_info);
        btn_work_save = (Button) findViewById(R.id.btn_work_save);
        from = getIntent().getStringExtra("work_from");
        intent_work = getIntent().getParcelableExtra("work_data");// 里面保存了所有参加活动的成员信息
        intent_work_objectId = getIntent().getStringExtra("intent_work_objectId");
        if ("add".equals(from)) {
            actionBar.setTitle(R.string.string_add_work);// 事务新建
        } else if ("look".equals(from)) {// 查看
            actionBar.setTitle(R.string.string_look);// 信息查看
            btn_work_save.setVisibility(View.GONE);
            members.clear();
            members.addAll(intent_work.getMembers());
            showWork(intent_work, false);
        } else if ("alter".equals(from)) {// 修改
            actionBar.setTitle(R.string.string_alter_work);// 信息修改//点击要删除
            members.clear();
            members.addAll(intent_work.getMembers());
            showWork(intent_work, true);
        }
        if (members.size() == 1) {// 没有参加成员，不显示
            tv_info.setVisibility(View.INVISIBLE);
        }
        adapter = new PersonAdapter(members);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        et_time.setFocusable(false);
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(WorkActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {
                        et_time.setText(year + "/" + (monthofYear + 1) + "/" + dayofMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        adapter.setOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                if ("look".equals(from)) {// 查看账单，RecyclerView跳到被点击成员的信息页面
                    Intent intent = new Intent(WorkActivity.this, MemberActivity.class);
                    intent.putExtra("member_from", "look");// 给MemberActivity的表示是查看
                    intent.putExtra("member_data", (Parcelable) members.get(position));
                    startActivity(intent);
                } else {
                    members.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        btn_work_save.setOnClickListener(this);
        et_work.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_work.setErrorEnabled(false);
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_address.setErrorEnabled(false);
            }
        });
        et_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                til_time.setErrorEnabled(false);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_work_save:
                if (TextUtils.isEmpty(et_work.getText().toString().trim())) {
                    til_work.setError("名称不能为空！");
                }
                if (TextUtils.isEmpty(et_address.getText().toString().trim())) {
                    til_work.setError("地点不能为空！");
                }
                if (TextUtils.isEmpty(et_time.getText().toString().trim())) {
                    til_work.setError("时间不能为空！");
                }
                if ("add".equals(from)) {
                    addWork();
                } else if ("alter".equals(from)) {
                    alterWork();
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

    private void addWork() {
        getWork(new ArrayList<Member>()).save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    showAlertDialog("创建数据成功");
                    et_work.setText("");
                    et_address.setText("");
                    et_time.setText("");
                    et_remark.setText("");
                } else {
                    BmobE.E(WorkActivity.this, e);
                }
            }
        });
    }

    private void alterWork() {
        getWork(members).update(intent_work_objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showAlertDialog("更新成功");
                } else {
                    BmobE.E(WorkActivity.this, e);
                }
            }
        });
    }

    private Work getWork(List<Member> members) {
        String name = et_work.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String date = et_time.getText().toString().trim();
        String remark = et_remark.getText().toString().trim();
        return new Work(name, address, date, remark, members, false);
    }

    /**
     * 传入false，EditText不可被编辑
     */
    private void showWork(Work work, boolean enabled) {
        et_work.setText(work.getName());
        et_address.setText(work.getAddress());
        et_time.setText(work.getDate());
        et_remark.setText(work.getRemark());
        if (!enabled) {
            et_work.setFocusable(false);
            et_address.setFocusable(false);
            et_time.setFocusable(false);
            et_remark.setFocusable(false);
            et_work.setEnabled(false);
            et_address.setEnabled(false);
            et_time.setEnabled(false);
            et_remark.setEnabled(false);
        }
    }

}
