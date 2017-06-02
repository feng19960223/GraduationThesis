package com.fgr.aabao.ui;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.fgr.aabao.R;

/**
 * 作者：Fgr on 2017/5/8 08:21
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：政策隐私
 */

public class TreatyActivity extends BaseActivity {
    @Override
    protected void init() {
        actionBar.setTitle(R.string.string_treaty);
        ((WebView) findViewById(R.id.wv_treaty)).loadUrl("file:///android_asset/treaty.html");
        //webView.removeJavascriptInterface();
    }

    @Override
    protected void initListener() {

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
