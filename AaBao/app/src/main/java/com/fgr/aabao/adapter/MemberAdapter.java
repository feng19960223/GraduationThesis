package com.fgr.aabao.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fgr.aabao.R;
import com.fgr.aabao.application.MyApp;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.ui.MemberActivity;
import com.fgr.aabao.utils.BmobE;
import com.fgr.aabao.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/9 08:11
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：成员的adapter
 */

public class MemberAdapter extends Adapter<ViewHolder> {

    private enum ITEM_TYPE {
        ITEM_TYPE_ADD, ITEM_TYPE_DATA
    }

    private static LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<Member> mMemberList;

    public MemberAdapter(ArrayList<Member> memberList) {
        mMemberList = memberList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        if (viewType == ITEM_TYPE.ITEM_TYPE_DATA.ordinal()) {
            return new DataViewHolder(mLayoutInflater.inflate(R.layout.item_member_data, parent, false));
        } else {
            return new AddViewHolder(mLayoutInflater.inflate(R.layout.item_member_add, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.ITEM_TYPE_ADD.ordinal() : ITEM_TYPE.ITEM_TYPE_DATA.ordinal();
    }

    private int[] colors = {
            R.mipmap.mc_card_bg1,
            R.mipmap.mc_card_bg2,
            R.mipmap.mc_card_bg3,
            R.mipmap.mc_card_bg4,
            R.mipmap.mc_card_bg5,
            R.mipmap.mc_card_bg6,
            R.mipmap.mc_card_bg7,
            R.mipmap.mc_card_bg8,
    };

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            DataViewHolder viewHolder = (DataViewHolder) holder;
            Glide.with(MyApp.getApplication()).load(colors[new Random().nextInt(7)]).into(viewHolder.iv_member);// 背景
            Member member = mMemberList.get(position);
            viewHolder.tv_item_member_name.setText(member.getName());
            viewHolder.tv_item_member_other.setText(getOther(member));
        }
    }

    @Override
    public int getItemCount() {
        return mMemberList == null ? 0 : mMemberList.size();
    }

    private class AddViewHolder extends ViewHolder {

        AddViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.btn_item_member_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MemberActivity.class);// 增加
                    intent.putExtra("member_from", "add");
                    UIUtils.startActivity(intent);
                }
            });
        }
    }

    private class DataViewHolder extends ViewHolder {
        TextView tv_item_member_name;
        TextView tv_item_member_other;
        ImageView iv_member;

        DataViewHolder(final View itemView) {
            super(itemView);
            tv_item_member_name = (TextView) itemView.findViewById(R.id.tv_item_member_name);
            tv_item_member_other = (TextView) itemView.findViewById(R.id.tv_item_member_other);
            iv_member = (ImageView) itemView.findViewById(R.id.iv_member);
            itemView.setOnClickListener(new View.OnClickListener() {// 查看
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MemberActivity.class);
                    intent.putExtra("member_from", "look");
                    intent.putExtra("member_data", (Parcelable) mMemberList.get(getLayoutPosition()));
                    UIUtils.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.btn_item_member_alter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//修改
                    Intent intent = new Intent(mContext, MemberActivity.class);
                    intent.putExtra("member_from", "alter");
                    intent.putExtra("intent_member_objectId", mMemberList.get(getLayoutPosition()).getObjectId());
                    intent.putExtra("member_data", (Parcelable) mMemberList.get(getLayoutPosition()));
                    UIUtils.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.btn_item_member_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {// 删除
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(R.string.string_warning);
                    alertDialog.setMessage(R.string.string_delete_what);
                    alertDialog.setNegativeButton(R.string.string_cancel, null);// 取消按钮
                    alertDialog.setPositiveButton(R.string.string_do, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mMemberList.get(getLayoutPosition()).delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        mMemberList.remove(getLayoutPosition());
                                        notifyDataSetChanged();
                                    } else {
                                        BmobE.E(mContext, e);
                                    }
                                }
                            });
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    private String getOther(Member member) {
        int i = 0;//最多显示 5 行数据，否则ui暴露
        StringBuilder other = new StringBuilder();
        if (!TextUtils.isEmpty(member.getPhone())) {
            other.append(member.getPhone() + "\n");
            i++;
        }
        if (!TextUtils.isEmpty(member.getDear())) {
            other.append(member.getDear() + "\n");
            i++;
        }
        if (!TextUtils.isEmpty(member.getCompany())) {
            other.append(member.getCompany() + "\n");
            i++;
        }
        if (!TextUtils.isEmpty(member.getPost())) {
            other.append(member.getPost() + "\n");
            i++;
        }
        if (i < 5 && !TextUtils.isEmpty(member.getAddress())) {
            other.append(member.getAddress() + "\n");
            i++;
        }
        if (i < 5 && !TextUtils.isEmpty(member.getBirthday())) {
            other.append(member.getBirthday() + "\n");
            i++;
        }
        if (i < 5 && !TextUtils.isEmpty(member.getOther())) {
            other.append(member.getOther() + "\n");
            i++;
        }
        if (i < 5 && !TextUtils.isEmpty(member.getRemark())) {
            other.append(member.getRemark());
        }
        return other.toString();
    }
}
