package com.co.codeconnectjudge.service.admin;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.po.Question;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
