package com.fgr.aabao.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.fgr.aabao.R;

import cn.bmob.v3.exception.BmobException;

/**
 * 作者：Fgr on 2017/5/14 08:52
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：处理BmobException
 */

public class BmobE {
    public static void E(Context context, BmobException e) {
        e.printStackTrace();
        Log.i("TAG_Bmob_Exception", "Exception: " + e.getMessage() + e.getErrorCode());
        switch (e.getErrorCode()) {
            case 9002:
                showAlertDialog(context, "解析返回数据出错！");
                break;
            case 9003:
                showAlertDialog(context, "上传文件出错！");
                break;
            case 9004:
                showAlertDialog(context, "文件上传失败！");
                break;
            case 9007:
                showAlertDialog(context, "文件大小超过10M！");
                break;
            case 9008:
                showAlertDialog(context, "上传文件不存在！");
                break;
            case 9009:
                showAlertDialog(context, "没有缓存数据！");
                break;
            case 9010:
                showAlertDialog(context, "网络超时！");
                break;
            case 9014:
                showAlertDialog(context, "第三方账号授权失败！");
                break;
            case 9015:
                showAlertDialog(context, "发生未知错误，请与开发者取得联系，尽快修复！");//其他错误均返回此code
                break;
            case 9016:
                showAlertDialog(context, "无网络连接，请检查您的手机网络！");
                break;
            case 9017:
                showAlertDialog(context, "与第三方登录有关的错误！");
                break;
            case 9018:
                showAlertDialog(context, "参数不能为空！");
                break;
            case 9019:
                showAlertDialog(context, "格式不正确：手机号码、邮箱地址、验证码！");
                break;
            case 9020:
                showAlertDialog(context, "保存CDN信息失败！");
                break;
            case 9021:
                showAlertDialog(context, "文件上传缺少wakelock权限！");
                break;
            case 9022:
                showAlertDialog(context, "文件上传失败，请重新上传！");
                break;
            case 401:
                showAlertDialog(context, "未被授权！");
                break;
            case 500:
                showAlertDialog(context, "服务器繁忙，稍后再试！");
                break;
            case 101:
                showAlertDialog(context, "登录的用户名或密码不正确！");
                break;
            case 102:
                showAlertDialog(context, "必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线！");
                break;
            case 108:
                showAlertDialog(context, "用户名和密码是必需的！");
                break;
            case 109:
                showAlertDialog(context, "登录信息是必需的，如邮箱和密码时缺少其中一个！");
                break;
            case 148:
                showAlertDialog(context, "空文件！");
                break;
            case 150:
                showAlertDialog(context, "文件上传错误！");
                break;
            case 202:
                showAlertDialog(context, "账号已经存在！");
                break;
            case 203:
                showAlertDialog(context, "邮箱已经存在！");
                break;
            case 204:
                showAlertDialog(context, "必须提供一个邮箱地址！");
                break;
            case 206:
                showAlertDialog(context, "登录用户才能修改自己的信息！");
                break;
            case 301:
                showAlertDialog(context, "邮箱地址不正确！");
                break;
            case 304:
                showAlertDialog(context, "用户名或密码不能为空！");
                break;
            default:
                showAlertDialog(context, e.getErrorCode() + ":" + e.getMessage());
        }
    }


    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.string_warning);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.string_do, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}
