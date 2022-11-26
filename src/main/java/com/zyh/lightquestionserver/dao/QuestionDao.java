package com.zyh.lightquestionserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.lightquestionserver.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionDao extends BaseMapper<Question> {
}
