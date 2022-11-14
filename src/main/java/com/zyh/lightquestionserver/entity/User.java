package com.zyh.lightquestionserver.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String username;            //用户名
    private String password;            //密码
    private String phone;               //手机号
    private Date date;                  //注册时间
    private String token;               //JWT生成的token
}
