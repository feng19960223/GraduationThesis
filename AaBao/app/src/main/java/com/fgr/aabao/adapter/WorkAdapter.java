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
import com.fgr.aabao.bean.Work;
import com.fgr.aabao.ui.AamActivity;
import com.fgr.aabao.ui.WorkActivity;
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

public class WorkAdapter extends Adapter<ViewHolder> {

    private enum ITEM_TYPE {
        ITEM_TYPE_ADD, ITEM_TYPE_DATA
    }

    private static LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<Work> mWorkList;

    public WorkAdapter(ArrayList<Work> WorkList) {
        mWorkList = WorkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        if (viewType == ITEM_TYPE.ITEM_TYPE_DATA.ordinal()) {
            return new DataViewHolder(mLayoutInflater.inflate(R.layout.item_work_data, parent, false));
        } else {
            return new AddViewHolder(mLayoutInflater.inflate(R.layout.item_work_add, parent, false));
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
            Glide.with(MyApp.getApplication()).load(colors[new Random().nextInt(7)]).into(viewHolder.iv_work);// 背景
            Work work = mWorkList.get(position);
            viewHolder.tv_item_work_name.setText(work.getName());
            viewHolder.tv_item_work_other.setText(getOther(work));
        }
    }

    @Override
    public int getItemCount() {
        return mWorkList == null ? 0 : mWorkList.size();
    }

    private class AddViewHolder extends ViewHolder {

        AddViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.btn_item_work_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WorkActivity.class);
                    intent.putExtra("work_from", "add");
                    UIUtils.startActivity(intent);
                }
            });
        }
    }

    private class DataViewHolder extends ViewHolder {
        TextView tv_item_work_name;
        TextView tv_item_work_other;
        ImageView iv_work;

        DataViewHolder(final View itemView) {
            super(itemView);
            tv_item_work_name = (TextView) itemView.findViewById(R.id.tv_item_work_name);
            tv_item_work_other = (TextView) itemView.findViewById(R.id.tv_item_work_other);
            iv_work = (ImageView) itemView.findViewById(R.id.iv_work);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WorkActivity.class);
                    intent.putExtra("work_from", "look");
                    intent.putExtra("work_data", (Parcelable) mWorkList.get(getLayoutPosition()));
                    UIUtils.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.btn_item_work_alter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WorkActivity.class);
                    intent.putExtra("work_from", "alter");
                    intent.putExtra("intent_work_objectId", mWorkList.get(getLayoutPosition()).getObjectId());
                    intent.putExtra("work_data", (Parcelable) mWorkList.get(getLayoutPosition()));
                    UIUtils.startActivity(intent);
                }
            });
            itemView.findViewById(R.id.btn_item_work_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(R.string.string_warning);
                    alertDialog.setMessage(R.string.string_delete_what);
                    alertDialog.setNegativeButton(R.string.string_cancel, null);// 取消按钮
                    alertDialog.setPositiveButton(R.string.string_do, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mWorkList.get(getLayoutPosition()).delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        mWorkList.remove(getLayoutPosition());
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
            itemView.findViewById(R.id.btn_item_work_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AamActivity.class);
                    intent.putExtra("work_data", (Parcelable) mWorkList.get(getLayoutPosition()));
                    intent.putExtra("intent_work_objectId", mWorkList.get(getLayoutPosition()).getObjectId());
                    UIUtils.startActivity(intent);//加入
                }
            });
            itemView.findViewById(R.id.btn_item_work_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //历史
                    Work work = mWorkList.get(getLayoutPosition());
                    work.setBill(true);
                    work.update(work.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mWorkList.remove(getLayoutPosition());
                                notifyDataSetChanged();
                            } else {
                                BmobE.E(mContext, e);
                            }
                        }
                    });
                }
            });
        }
    }

    private String getOther(Work work) {
        StringBuilder other = new StringBuilder();
        if (!TextUtils.isEmpty(work.getAddress())) {
            other.append(work.getAddress() + "\n");
        }
        if (!TextUtils.isEmpty(work.getDate())) {
            other.append(work.getDate() + "\n");
        }
        if (!TextUtils.isEmpty(work.getRemark())) {
            other.append(work.getRemark());
        }
        return other.toString();
    }
}
