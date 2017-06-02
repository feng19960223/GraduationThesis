package com.fgr.aabao.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.bean.MyUser;
import com.fgr.aabao.utils.BmobE;
import com.fgr.aabao.utils.DataCleanManager;
import com.fgr.aabao.utils.HttpUtils;
import com.fgr.aabao.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 作者：Fgr on 2017/5/10 15:20
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：设置页面
 */

public class SetActivity extends BaseActivity {
    private CircleImageView icon;
    private ImageView icon_bg;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextInputLayout ll_set_dear;
    private TextInputLayout ll_set_mail;
    private EditText et_set_dear;
    private EditText et_set_mail;
    private Button btn_set_activate;// 邮箱验证
    private Button btn_set_cache;// 缓存清理
    private MyUser myUser;
    String cameraPath;// 拍摄头像照片时SD卡的路径
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void init() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        icon = (CircleImageView) findViewById(R.id.icon);
        icon_bg = (ImageView) findViewById(R.id.icon_bg);
        btn_set_activate = (Button) findViewById(R.id.btn_set_activate);
        btn_set_cache = (Button) findViewById(R.id.btn_set_cache);
        ll_set_dear = (TextInputLayout) findViewById(R.id.ll_set_dear);
        ll_set_mail = (TextInputLayout) findViewById(R.id.ll_set_mail);
        et_set_dear = (EditText) findViewById(R.id.et_set_dear);
        et_set_mail = (EditText) findViewById(R.id.et_set_mail);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = pref.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(icon_bg);
        } else {
            loadBingPic();
        }
        myUser = MyUser.getCurrentUser(MyUser.class);
        if (myUser.getIcon() != null) {
            Glide.with(this).load(myUser.getIcon().getUrl()).into(icon);
        }
        if (myUser.getDear() != null) {
            collapsingToolbarLayout.setTitle(myUser.getDear());
            et_set_dear.setText(myUser.getDear());
        } else {
            et_set_dear.setText(myUser.getUsername());
        }
        if (myUser.getEmailVerified()) {// 已经验证了邮箱，把该按钮隐藏
            btn_set_activate.setVisibility(View.GONE);
        }
        et_set_mail.setText(myUser.getEmail());
        btn_set_cache.setText("" + getString(R.string.string_cache) + DataCleanManager.getTotalCacheSize(this));
    }

    @Override
    protected void initListener() {
        icon.setOnClickListener(this);
        findViewById(R.id.floating_action).setOnClickListener(this);
        findViewById(R.id.btn_set_save).setOnClickListener(this);
        findViewById(R.id.btn_set_mail).setOnClickListener(this);
        findViewById(R.id.btn_set_cache).setOnClickListener(this);
        findViewById(R.id.btn_set_password).setOnClickListener(this);
        findViewById(R.id.btn_set_activate).setOnClickListener(this);
        findViewById(R.id.btn_set_quit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action:
                setAvatar();
                break;
            case R.id.icon:
                setAvatar();
                break;
            case R.id.btn_set_save:
                saveDear();
                break;
            case R.id.btn_set_mail:
                changeEmail();
                break;
            case R.id.btn_set_cache:
                DataCleanManager.clearAllCache(this);
                btn_set_cache.setText("" + UIUtils.getString(R.string.string_cache) + DataCleanManager.getTotalCacheSize(this));
                break;
            case R.id.btn_set_password:
                password();
                break;
            case R.id.btn_set_activate:
                activate();
                break;
            case R.id.btn_set_quit:
                MyUser.logOut();   //清除缓存用户对象
                jumpTo(LoginActivity.class, true);
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

    public void setAvatar() {
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis() + ".jpg");
        cameraPath = file.getAbsolutePath();
        Uri uri = Uri.fromFile(file);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        Intent chooser = Intent.createChooser(intent1, getString(R.string.string_icon));
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent2});
        startActivityForResult(chooser, 101);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        try {
            super.onActivityResult(arg0, arg1, arg2);
            if (arg1 == RESULT_OK) {
                if (arg0 == 101) {
                    String filePath;
                    if (arg2 != null) {
                        Uri uri = arg2.getData();
                        filePath = uri.getPath();
                    } else {
                        // 相机拍照
                        filePath = cameraPath;
                    }
                    crop(filePath);
                }
                if (arg0 == 102) {
                    // 获得了系统截图程序返回的截取后的图片
                    final Bitmap bitmap = arg2.getParcelableExtra("data");
                    // 上传前，要将bitmap保存到SD卡
                    // 获得保存路径后，再上传
                    final File file = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            System.currentTimeMillis() + ".jpg");
                    OutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // 要使用三个参数的
                    final BmobFile bf = new BmobFile(myUser.getUsername(), null, file.toString());
                    showProgressDialog();
                    MyUser newMyUser = new MyUser();
                    newMyUser.setIcon(bf);
                    newMyUser.update(myUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Glide.with(SetActivity.this).load(file.getPath()).into(icon);
                            } else {
                                BmobE.E(SetActivity.this, e);
                            }
                            closeProgressDialog();
                        }
                    });
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用安卓的图片剪裁程序对用户选择的头像进行剪裁
     *
     * @param filePath
     *         用户选取的头像在SD上的地址
     */
    private void crop(String filePath) {
        // 隐式intent
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri data = Uri.fromFile(new File(filePath));
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        startActivityForResult(intent, 102);
    }

    private void saveDear() {
        final String dear = et_set_dear.getText().toString().trim();
        MyUser newUser = new MyUser();
        newUser.setDear(dear);
        newUser.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(SetActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                    collapsingToolbarLayout.setTitle(dear);
                    et_set_dear.setText(myUser.getDear());
                } else {
                    BmobE.E(SetActivity.this, e);
                }
            }
        });
    }

    private void changeEmail() {
        final String email = et_set_mail.getText().toString().trim();
        MyUser newUser = new MyUser();
        newUser.setEmail(email);
        newUser.update(myUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(SetActivity.this, "请到邮箱就行更改", Toast.LENGTH_SHORT).show();
                    et_set_mail.setText("");
                } else {
                    BmobE.E(SetActivity.this, e);
                }
            }
        });
    }

    private void password() {
        MyUser.resetPasswordByEmail(myUser.getEmail(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showAlertDialog("重置密码请求成功，请到" + myUser.getEmail() + "邮箱进行密码重置操作。");
                } else {
                    BmobE.E(SetActivity.this, e);
                }
            }
        });
    }

    private void activate() {

        MyUser.requestEmailVerify(myUser.getEmail(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showAlertDialog("请求验证邮件成功，请到" + myUser.getEmail() + "邮箱中进行激活。");
                } else {
                    BmobE.E(SetActivity.this, e);
                }
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                editor = pref.edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SetActivity.this).load(bingPic).into(icon_bg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
