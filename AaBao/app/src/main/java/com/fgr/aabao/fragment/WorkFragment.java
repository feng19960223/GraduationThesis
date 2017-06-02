package com.fgr.aabao.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.fgr.aabao.R;
import com.fgr.aabao.adapter.WorkAdapter;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.bean.Work;
import com.fgr.aabao.utils.BmobE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：Fgr on 2017/4/30 09:29
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：活动管理
 */

public class WorkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private WorkAdapter adapter;
    private ArrayList<Work> works = new ArrayList<>();

    @Override
    public void init() {
        swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.sf_work);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview_work);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        works.clear();
        works.add(null);// 加一个空，用于显示第一个item
        queryWorks();
        adapter = new WorkAdapter(works);
        recyclerView.setAdapter(adapter);
    }

    private boolean isFrist = true;

    private void queryWorks() {
        BmobQuery<Work> query = new BmobQuery<>();
        query.addWhereEqualTo("id", BmobUser.getCurrentUser(MyUser.class).getObjectId())
                .addWhereEqualTo("isBill", false)//
                .setLimit(1000)
                .order("-updatedAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        if (isFrist) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            isFrist = false;
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Work>() {
            @Override
            public void done(List<Work> object, BmobException e) {
                works.clear();
                works.add(null);// 显示增加item
                if (e == null) {
                    works.addAll(object);
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
        queryWorks();
    }

    @Override
    public void onRefresh() {
        queryWorks();
    }

}
