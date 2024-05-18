package com.co.backend.service.oj.impl;

import com.co.backend.service.oj.JudgeService;
import com.co.common.exception.StatusFailException;
import com.co.common.exception.StatusForbiddenException;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.common.result.ResultStatus;
import com.co.backend.manager.oj.JudgeManager;
import com.co.backend.model.dto.SubmitJudgeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:52
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    private JudgeManager judgeManager;
    @Override
    public ResponseResult submitJudgeQuestion(SubmitJudgeDTO submitJudgeDTO) {

        try {
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(),"判题提交成功",judgeManager.submitJudgeQuestion(submitJudgeDTO));
        } catch (StatusForbiddenException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        } catch (StatusFailException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }


    }
}
