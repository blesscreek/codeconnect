package com.co.codeconnectjudge.service.oj;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.dto.SubmitJudgeDTO;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:52
 */

public interface JudgeService {
    ResponseResult submitJudgeQuestion(SubmitJudgeDTO submitJudgeDTO);
}
