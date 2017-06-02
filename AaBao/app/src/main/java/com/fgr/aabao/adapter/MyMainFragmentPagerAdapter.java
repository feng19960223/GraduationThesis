package com.fgr.aabao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fgr.aabao.R;
import com.fgr.aabao.fragment.FragmentFactory;
import com.fgr.aabao.utils.UIUtils;

/**
 * 作者：Fgr on 2017/4/30 15:33
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：主界面Fragment的Adapter
 */

public class MyMainFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] tab_names;

    public MyMainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tab_names = UIUtils.getStringArray(R.array.tab_names);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_names[position];
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return tab_names.length;
    }
}
