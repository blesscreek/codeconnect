package com.co.backend.service.admin;

import com.co.backend.model.po.PageParams;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.dto.GetQuestionListDTO;
import com.co.backend.model.dto.QuestionDTO;
import com.co.common.exception.StatusFailException;
import com.co.common.exception.StatusForbiddenException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author co
 * @since 2024-04-07
 */
public interface AdminQuestionService  {

    ResponseResult getQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO);
    ResponseResult getEditQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO);

    ResponseResult showQuestion(Long qid);

    ResponseResult addQuestion(QuestionDTO questionDTO);

    ResponseResult deleteQuestion(Long qid);

    ResponseResult getQuestion(Long qid);

    ResponseResult updateQuestion(QuestionDTO questionDTO);


    ResponseResult getHotQuestion();

}
