package com.fgr.aabao.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.fgr.aabao.R;
import com.fgr.aabao.adapter.MemberAdapter;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.utils.BmobE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：Fgr on 2017/4/30 09:08
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：会员管理
 */

public class MemberFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private ArrayList<Member> members = new ArrayList<>();

    @Override
    public void init() {
        swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.sf_member);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview_member);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        members.clear();
        members.add(null);// 加一个空，用于显示第一个item
        //查询所有成员
        queryMembers();
        adapter = new MemberAdapter(members);
        recyclerView.setAdapter(adapter);
    }

    private boolean isFrist = true;

    private void queryMembers() {
        BmobQuery<Member> query = new BmobQuery<>();
        query.addWhereEqualTo("id", BmobUser.getCurrentUser(MyUser.class).getObjectId())
                .setLimit(1000).order("-updatedAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        if (isFrist) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            isFrist = false;
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Member>() {
            @Override
            public void done(List<Member> object, BmobException e) {
                members.clear();
                members.add(null);// 显示增加item
                if (e == null) {
                    members.addAll(object);
                    adapter.notifyDataSetChanged();
                } else {
                    BmobE.E(getActivity(), e);
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        queryMembers();
    }

    @Override
    public void onRefresh() {
        queryMembers();
    }
}
