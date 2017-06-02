package com.fgr.aabao.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fgr.aabao.R;
import com.fgr.aabao.bean.Work;
import com.fgr.aabao.ui.WorkActivity;
import com.fgr.aabao.utils.BmobE;
import com.fgr.aabao.utils.UIUtils;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 作者：Fgr on 2017/5/10 11:20
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：记录adapter
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private Context mContext;
    private ArrayList<Work> mWorkList;

    public BillAdapter(ArrayList<Work> workList) {
        mWorkList = workList;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Work work = mWorkList.get(position);
        holder.tv_bill_title.setText(work.getName());
        holder.tv_bill_time.setText(work.getUpdatedAt());
    }

    @Override
    public int getItemCount() {
        return mWorkList == null ? 0 : mWorkList.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bill_title;
        TextView tv_bill_time;

        BillViewHolder(View view) {
            super(view);
            tv_bill_title = (TextView) view.findViewById(R.id.tv_bill_title);
            tv_bill_time = (TextView) view.findViewById(R.id.tv_bill_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 进入历史详情页面,查看
                    Intent intent = new Intent(mContext, WorkActivity.class);
                    intent.putExtra("work_from", "look");
                    intent.putExtra("work_data", (Parcelable) mWorkList.get(getLayoutPosition()));
                    UIUtils.startActivity(intent);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(R.string.string_warning);
                    alertDialog.setMessage("确定要删除吗？");
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
                    return false;
                }
            });
        }
    }

}
