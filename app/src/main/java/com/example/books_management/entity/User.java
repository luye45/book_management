package com.example.books_management.entity;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {
    private String id; // 用户名
    private String name; // 姓名
    private String password; // 密码
    private String sex; // 性别
    private Date birth;//出生年月
    private String mobile; // 手机号

    public User(String id, String name, String password, String sex, Date birth, String mobile) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birth = birth;
        this.mobile = mobile;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setbirth(Date birth) {
        this.birth = birth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
