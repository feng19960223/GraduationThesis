package com.fgr.aabao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fgr.aabao.R;
import com.fgr.aabao.bean.Member;
import com.fgr.aabao.inter.OnMyItemClickListener;

import java.util.List;

/**
 * 作者：Fgr on 2017/5/10 11:20
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private Context mContext;
    private List<Member> mMemberList;

    public PersonAdapter(List<Member> memberList) {
        mMemberList = memberList;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_member_list, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Member member = mMemberList.get(position);
        holder.textview_title.setText(member.getName());
        holder.textview_body.setText(getOther(member));
    }

    @Override
    public int getItemCount() {
        return mMemberList == null ? 0 : mMemberList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView textview_body;
        TextView textview_title;

        PersonViewHolder(View view) {
            super(view);
            textview_title = (TextView) view.findViewById(R.id.textview_title);
            textview_body = (TextView) view.findViewById(R.id.textview_body);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getLayoutPosition());
                }
            });
        }

    }

    private OnMyItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String getOther(Member member) {
        String other = "";
        if (!TextUtils.isEmpty(member.getPhone())) {
            other = member.getPhone();
        }
        if (!TextUtils.isEmpty(member.getDear())) {
            other = member.getDear();
        }
        if (!TextUtils.isEmpty(member.getCompany())) {
            other = member.getCompany();
        }
        if (!TextUtils.isEmpty(member.getPost())) {
            other = member.getPost();
        }
        if (!TextUtils.isEmpty(member.getAddress())) {
            other = member.getAddress();
        }
        if (!TextUtils.isEmpty(member.getBirthday())) {
            other = member.getBirthday();
        }
        if (!TextUtils.isEmpty(member.getOther())) {
            other = member.getOther();
        }
        if (!TextUtils.isEmpty(member.getRemark())) {
            other = member.getRemark();
        }
        return other;
    }

}
