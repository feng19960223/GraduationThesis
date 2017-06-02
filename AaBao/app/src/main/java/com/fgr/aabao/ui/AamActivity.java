package com.fgr.aabao.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.fgr.aabao.R;
import com.fgr.aabao.adapter.PersonAdapter;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.bean.Work;
import com.fgr.aabao.inter.OnMyItemClickListener;
import com.fgr.aabao.utils.BmobE;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/9 17:36
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：Activity Add Member,活动增加人员的Activity
 */

public class AamActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<Member> members = new ArrayList<>();//所有数据
    private List<Member> haveMembers = new ArrayList<>();//已有数据
    private Work intent_work;
    private String intent_work_objectId;

    @Override
    protected void init() {
        actionBar.setTitle(R.string.string_add_person);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_aam);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        intent_work = getIntent().getParcelableExtra("work_data");// 里面保存了所有参加活动的成员信息
        intent_work_objectId = getIntent().getStringExtra("intent_work_objectId");
        haveMembers = intent_work.getMembers();
        queryMembers();//allMembers 已经有数据
        members.removeAll(haveMembers);// 所有数据-已有数据就是满足需求的人，也是可添加的人
        adapter = new PersonAdapter(members);
        recyclerView.setAdapter(adapter);
    }

    private void queryMembers() {
        showProgressDialog();
        BmobQuery<Member> query = new BmobQuery<>();
        query.addWhereEqualTo("id", BmobUser.getCurrentUser(MyUser.class).getObjectId())
                .setLimit(1000).findObjects(new FindListener<Member>() {
            @Override
            public void done(List<Member> object, BmobException e) {
                members.clear();
                if (e == null) {
                    members.addAll(object);
                    members.removeAll(haveMembers);// 所有数据-已有数据就是满足需求的人，也是可添加的人
                    adapter.notifyDataSetChanged();
                } else {
                    BmobE.E(AamActivity.this, e);
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new OnMyItemClickListener() {
            @Override
            public void onItemClickListener(final int position) {
                haveMembers.add(members.get(position));
                intent_work.setMembers(haveMembers);
                intent_work.update(intent_work_objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            members.remove(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            BmobE.E(AamActivity.this, e);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
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
