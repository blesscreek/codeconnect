package com.co.backend.dao.question;

import com.baomidou.mybatisplus.extension.service.IService;
import com.co.backend.model.po.Question;
import com.co.common.exception.StatusFailException;
import com.co.backend.model.dto.QuestionDTO;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 17:26
 */

public interface QuestionEntityService extends IService<Question> {
    boolean addQuestion(QuestionDTO questionDTO) throws StatusFailException;

    List<Question> selectQuestions(String[] tagNames, String titleKeyword, Integer difficulty, Long pageSize, Long offset);

    boolean updateQuestion(QuestionDTO questionDTO) throws StatusFailException;
}
