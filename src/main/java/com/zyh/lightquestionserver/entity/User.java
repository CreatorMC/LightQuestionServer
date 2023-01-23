package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zyh.lightquestionserver.annotation.EncryptField;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.ASSIGN_ID)   //雪花算法生成ID，必须使用Long而不是long！
    private Long id;
    private String username;            //用户名

    @EncryptField                       //此字段需要被加密（自定义注解）
    private String password;            //密码

    @EncryptField
    private String email;               //电子邮箱

    @EncryptField
    private String phone;               //手机号

    private Date date;                  //注册时间
}
