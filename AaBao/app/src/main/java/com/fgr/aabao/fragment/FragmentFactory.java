package com.fgr.aabao.fragment;

import java.util.HashMap;

/**
 * 作者：Fgr on 2017/4/30 09:39
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：产生Fragment，Fragment工厂
 */

public class FragmentFactory {
    private static final int TAB_MEMBER_MANAGER = 0;
    private static final int TAB_ACTIVITY_MANAGER = 1;
    private static final int TAB_BILL_MANAGER = 2;
    private static final int TAB_MAP = 3;
    private static final int TAB_WEATHER = 4;
    private static HashMap<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();
    private static BaseFragment fragment;

    public static BaseFragment createFragment(int position) {
        fragment = mFragments.get(position);// 获取到内存里面是否有Fragment
        if (null == fragment) {// 内存里面没有
            switch (position) {
                case TAB_MEMBER_MANAGER:
                    fragment = new MemberFragment();
                    break;
                case TAB_ACTIVITY_MANAGER:
                    fragment = new WorkFragment();
                    break;
                case TAB_BILL_MANAGER:
                    fragment = new BillFragment();
                    break;
                case TAB_MAP:
                    fragment = new MapFragment();
                    break;
                case TAB_WEATHER:
                    fragment = new WeatherFragment();
                    break;
                default:
            }
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
