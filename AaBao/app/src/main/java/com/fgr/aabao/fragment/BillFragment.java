package com.fgr.aabao.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fgr.aabao.R;
import com.fgr.aabao.adapter.BillAdapter;
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
 * 说明：账单管理
 */

public class BillFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private BillAdapter adapter;
    private ArrayList<Work> works = new ArrayList<>();

    @Override
    public void init() {
        swipeRefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.sf_bill);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview_bill);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());//增加删除动画
        works.clear();
        queryBills();
        adapter = new BillAdapter(works);
        recyclerView.setAdapter(adapter);
    }

    private boolean isFrist = true;

    private void queryBills() {
        BmobQuery<Work> query = new BmobQuery<>();
        query.addWhereEqualTo("id", BmobUser.getCurrentUser(MyUser.class).getObjectId())
                .addWhereEqualTo("isBill", true)// 是false的在事务里面显示
                .setSkip(works.size())// 忽略已经有的条目
                .setLimit(10)// 每次查询多少条
                .order("-updatedAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        // boolean isCache = query.hasCachedResult(Member.class);
        if (isFrist) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            //   query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Work>() {
            @Override
            public void done(List<Work> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        adapter.notifyDataSetChanged();
                        works.addAll(object);
                        if (!isFrist) {// 第一次加载不提示
                            Toast.makeText(getActivity(), "刷新了" + object.size() + "条记录！", Toast.LENGTH_SHORT).show();
                        }
                    } else if (object.size() == 0) {
                        showAlertDialog("没有记录了");
                    }
                    isFrist = false;
                } else {
                    BmobE.E(getActivity(), e);
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        queryBills();
    }

}
