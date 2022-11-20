package com.zyh.lightquestionserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.lightquestionserver.entity.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionBankDao extends BaseMapper<QuestionBank> {
}
