package com.co.codeconnectjudge.manager.admin;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.dao.question.QuestionService;
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
        boolean addRes = questionService.addQuestion(questionDTO);
        if (addRes == false) {
            throw new StatusFailException("题目添加失败");
        }
    }

}
