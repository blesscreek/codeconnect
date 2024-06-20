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

    ResponseResult addQuestion(QuestionDTO questionDTO);

    ResponseResult getQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO);

    ResponseResult getQuestion(Long qid);

}
