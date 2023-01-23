package com.zyh.lightquestionserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
/*
  前端发过来的邮箱验证码接收类
 */
public class UserEmailRegisterVCode {
    private String email;
    private String password;
    private String uuid;
    @JsonProperty("vCode")      //神坑...一个字母拼一个单词的驼峰命名不能正确接收前端传过来的值，要加这个注解才行
    private String vCode;
}
