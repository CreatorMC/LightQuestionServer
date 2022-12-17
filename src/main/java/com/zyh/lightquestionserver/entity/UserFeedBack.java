package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_feedback")
public class UserFeedBack {
    @TableId(type = IdType.AUTO)
    private Integer id;             //自增ID
    private Long userid;            //用户ID
    private String content;         //反馈的内容
}
