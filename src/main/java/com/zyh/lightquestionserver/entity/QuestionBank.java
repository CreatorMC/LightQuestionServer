package com.zyh.lightquestionserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("question_bank")
public class QuestionBank {
    @TableId
    private String id;
    private String title;
    private String label;
    private String tablename;
    private String icon;
}
