package com.fgr.aabao.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fgr.aabao.R;
import com.fgr.aabao.utils.UIUtils;

import java.util.Locale;

/**
 * 作者：Fgr on 2017/4/30 09:35
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public abstract class BaseFragment extends Fragment {
    protected View contentView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // SettingFragment;
        String clazzName = getClass().getSimpleName();
        if (clazzName.contains("Fragment")) {
            // "标准"的布局文件命名应该是:fragment_setting
            String layoutName = clazzName.substring(0,
                    clazzName.indexOf("Fragment")).toLowerCase(Locale.US);
            layoutName = "fragment_" + layoutName;
            // R.layout.fragment_setting
            int resId = getResources().getIdentifier(layoutName, "layout",
                    getActivity().getPackageName());

            if (resId != 0) {
                contentView = inflater.inflate(resId, container, false);// 利用反射，加载布局
            }
        }
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init() {
        // No-OP 钩子方法
    }

    private AlertDialog.Builder alertDialog;

    protected void showAlertDialog(String message) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(R.string.string_warning);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(R.string.string_do, null);
        }
        alertDialog.setMessage(message);
        alertDialog.show();
    }

}
