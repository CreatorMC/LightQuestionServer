package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table")
public class Question {
    Integer type;               //题目类型
    @TableId
    Integer number;             //题号
    String topic;               //题目描述
    @TableField("OptionA")
    String OptionA;             //选项A
    @TableField("OptionB")
    String OptionB;             //选项B
    @TableField("OptionC")
    String OptionC;             //选项C
    @TableField("OptionD")
    String OptionD;             //选项D
    Integer fraction;           //本题分数
    String answer;              //答案
}
