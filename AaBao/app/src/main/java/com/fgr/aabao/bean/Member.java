package com.fgr.aabao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * 作者：Fgr on 2017/5/9 13:17
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：
 */

public class Member extends BmobObject implements Parcelable {
    private String name;// 名字
    private String phone;// 手机
    private String company;// 公司
    private String post;// 职位
    private String address;// 地址
    private String dear;// 称呼
    private String birthday;// 生日
    private String other;// 其他联系方式
    private String remark;// 备注
    private String id;

    public Member() {
    }

    public Member(String name, String phone, String company, String post, String address, String dear, String birthday, String other, String remark) {
        this.name = name;
        this.phone = phone;
        this.company = company;
        this.post = post;
        this.address = address;
        this.dear = dear;
        this.birthday = birthday;
        this.other = other;
        this.remark = remark;
        this.id = BmobUser.getCurrentUser().getObjectId();//成员默认属于当前用户
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return  false;
        }
        if(this==obj){
            return true;
        }
        if(obj instanceof Member){
            Member m = (Member)obj;
            return (m.getObjectId()).equals(this.getObjectId());
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(company);
        parcel.writeString(post);
        parcel.writeString(address);
        parcel.writeString(dear);
        parcel.writeString(birthday);
        parcel.writeString(other);
        parcel.writeString(remark);
        parcel.writeString(id);
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel parcel) {
            Member member = new Member();
            member.name = parcel.readString();
            member.phone = parcel.readString();
            member.company = parcel.readString();
            member.post = parcel.readString();
            member.address = parcel.readString();
            member.dear = parcel.readString();
            member.birthday = parcel.readString();
            member.other = parcel.readString();
            member.remark = parcel.readString();
            member.id = parcel.readString();
            return member;
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDear() {
        return dear;
    }

    public void setDear(String dear) {
        this.dear = dear;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}