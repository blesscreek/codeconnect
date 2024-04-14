package com.co.codeconnectjudge.service.admin.impl;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.common.result.ResultStatus;
import com.co.codeconnectjudge.manager.admin.AdminQuestionManager;
import com.co.codeconnectjudge.model.dto.GetQuestionListDTO;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.dto.QuestionListReturnDTO;
import com.co.codeconnectjudge.model.po.PageParams;
import com.co.codeconnectjudge.service.admin.AdminQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-04-07
 */
@Service
public class AdminQuestionServiceImpl implements AdminQuestionService {

    @Autowired
    private AdminQuestionManager adminQuestionManager;
    @Override
    public ResponseResult addQuestion(QuestionDTO questionDTO) {
        try {
            adminQuestionManager.addQuestion(questionDTO);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目添加成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }

    }

    @Override
    public ResponseResult getQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目查找成功",adminQuestionManager.getQuestionList(pageParams, getQuestionListDTO));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }
}
