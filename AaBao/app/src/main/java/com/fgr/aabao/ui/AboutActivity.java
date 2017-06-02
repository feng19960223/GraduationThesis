package com.fgr.aabao.ui;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fgr.aabao.R;
import com.fgr.aabao.utils.PackageUtils;

/**
 * 作者：Fgr on 2017/5/3 09:51
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class AboutActivity extends BaseActivity {
    @Override
    protected void init() {
        actionBar.setTitle(R.string.string_menu_about);
        TextView textView = (TextView) findViewById(R.id.tv_version);
        textView.setText("V " + PackageUtils.getVersionCode() + "." + PackageUtils.getVersionName());
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

    @Override
    protected void initListener() {
    }

    @Override
    public void onClick(View view) {
    }
}
