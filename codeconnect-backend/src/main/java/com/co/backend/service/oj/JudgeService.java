package com.co.backend.service.oj;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.dto.SubmitJudgeDTO;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:52
 */

public interface JudgeService {
    ResponseResult submitJudgeQuestion(SubmitJudgeDTO submitJudgeDTO);
}
