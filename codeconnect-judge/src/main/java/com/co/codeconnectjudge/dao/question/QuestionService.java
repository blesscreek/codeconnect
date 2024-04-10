package com.co.codeconnectjudge.dao.question;

import com.baomidou.mybatisplus.extension.service.IService;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.po.Question;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 17:26
 */

public interface QuestionService extends IService<Question> {
    boolean addQuestion(QuestionDTO questionDTO);
}
