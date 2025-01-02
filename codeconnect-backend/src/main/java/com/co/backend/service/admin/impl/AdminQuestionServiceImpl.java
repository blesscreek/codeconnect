package com.co.backend.service.admin.impl;

import com.co.backend.model.po.PageParams;
import com.co.common.exception.StatusFailException;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.common.result.ResultStatus;
import com.co.backend.manager.admin.AdminQuestionManager;
import com.co.backend.model.dto.GetQuestionListDTO;
import com.co.backend.model.dto.QuestionDTO;
import com.co.backend.service.admin.AdminQuestionService;
import com.co.common.exception.StatusForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ResponseResult getQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目查找成功",adminQuestionManager.getQuestionList(pageParams, getQuestionListDTO));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult getEditQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "编辑题目查找成功",adminQuestionManager.getEditQuestionList(pageParams, getQuestionListDTO));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult showQuestion(Long qid) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目展示成功",adminQuestionManager.showQuestion(qid));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        } catch (StatusForbiddenException e) {
            return new ResponseResult(ResultStatus.FORBIDDEN.getStatus(), e.getMessage());
        }
    }

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
    public ResponseResult deleteQuestion(Long qid) {
        try {
            adminQuestionManager.deleteQuestion(qid);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目删除成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult getQuestion(Long qid) {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目详情获取成功", adminQuestionManager.getQuestion(qid));
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult updateQuestion(QuestionDTO questionDTO) {
        try {
            adminQuestionManager.updateQuestion(questionDTO);
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "题目更改成功");
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResponseResult getHotQuestion() {
        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(),"热门题目获取成功", adminQuestionManager.hotQuestion());
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
    }

}
