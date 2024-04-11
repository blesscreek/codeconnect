package com.co.codeconnectjudge.manager.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.dao.question.QuestionService;
import com.co.codeconnectjudge.model.po.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-07 22:38
 */

@Component
public class AdminQuestionManager {
    @Autowired
    private QuestionService questionService;
    public void addQuestion(QuestionDTO questionDTO) throws StatusFailException {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",questionDTO.getQuestion().getTitle());
        Question question = questionService.getOne(queryWrapper);
        if (question != null) {
            throw new StatusFailException("题目标题重复，请更换");
        }
        boolean addRes = questionService.addQuestion(questionDTO);
        if (addRes == false) {
            throw new StatusFailException("题目添加失败");
        }
    }

}
