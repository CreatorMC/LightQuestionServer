package com.zyh.lightquestionserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyh.lightquestionserver.dao.QuestionBankDao;
import com.zyh.lightquestionserver.entity.QuestionBank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Data")
public class DataController {

    @Autowired
    QuestionBankDao questionBankDao;

    /**
     * 获取指定类型的一类题单
     * @param id 前端传的类型id，比如0101，就是获取计算机文化基础这个科目下的所有题单
     * @return List<QuestionBank>
     */
    @GetMapping("/getQuestionBank/{id}")
    public List<QuestionBank> getQuestionBank(@PathVariable(value = "id") String id) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("id", id);
        return questionBankDao.selectList(queryWrapper);
    }
}
