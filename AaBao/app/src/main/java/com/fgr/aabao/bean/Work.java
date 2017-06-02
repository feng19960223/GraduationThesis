package com.fgr.aabao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * 作者：Fgr on 2017/5/9 16:11
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Work extends BmobObject implements Parcelable {
    private String name;
    private String address;
    private String date;
    private String remark;
    private List<Member> members;// 有哪些成员
    private String id;// 属于哪个用户
    private boolean isBill;// 是否为历史

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(date);
        parcel.writeString(remark);
        parcel.writeList(members);
    }

    public static final Parcelable.Creator<Work> CREATOR = new Parcelable.Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel parcel) {
            Work work = new Work();
            work.name = parcel.readString();
            work.address = parcel.readString();
            work.date = parcel.readString();
            work.remark = parcel.readString();
            work.members = parcel.readArrayList(Member.class.getClassLoader());
            return work;
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };


    public Work() {
    }

    public Work(String name, String address, String date, String remark, List<Member> members, boolean isBill) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.remark = remark;
        this.members = members;
        this.isBill = isBill;
        this.id = BmobUser.getCurrentUser().getObjectId();// 历史记录默认属于当前用户
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public boolean isBill() {
        return isBill;
    }

    public void setBill(boolean bill) {
        isBill = bill;
    }
}
