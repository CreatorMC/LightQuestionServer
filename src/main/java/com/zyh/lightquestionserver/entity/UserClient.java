package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
/*
  发给前端存储在localStorage里的用户对象
 */
public class UserClient {
    private Long id;                    //ID，给到前端之后，前端发请求时可以带上
    private String username;            //用户名
//    private String phone;               //手机号
    private Date date;                  //注册时间
    private String token;               //JWT的Token
}
