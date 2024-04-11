package com.co.codeconnectjudge.dao.question;

import com.baomidou.mybatisplus.extension.service.IService;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.po.Question;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.PriorityQueue;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 17:26
 */

public interface QuestionService extends IService<Question> {
    boolean addQuestion(QuestionDTO questionDTO) throws StatusFailException;

    List<Question> selectQuestions(String[] tagNames, String titleKeyword, Integer difficulty, Long pageSize, Long offset);
}
