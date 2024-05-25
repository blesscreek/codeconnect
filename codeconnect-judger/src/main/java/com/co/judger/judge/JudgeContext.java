package com.co.judger.judge;

import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.model.Judge;
import com.co.judger.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author co
 * @Version 1.0
 * @Description 各类判题的题目限制设置和判题结果的设置返回
 * @Date 2024-05-16 21:26
 */
@Component
public class JudgeContext {
    @Autowired
    private JudgeStrategy judgeStrategy;

    public Judge judge(Question question, JudgeInfo judgeInfo) {
        //c和c++为一倍时间和空间，其它语言为2倍时间和空间
        if (!LanguageConstants.Language.C.getLanguage().equals(judgeInfo.getLanguage())
                && !LanguageConstants.Language.CPP.getLanguage().equals(judgeInfo.getLanguage())) {
            question.setTimeLimit(question.getTimeLimit() * 2);
            question.setMemoryLimit(question.getMemoryLimit() * 2);
        }
        //进入判题
        HashMap<String, Object> judgeResult = judgeStrategy.judge(question, judgeInfo);

        Judge finalJudgeRes = new Judge();
        finalJudgeRes.setId(judgeInfo.getId());
        if (judgeResult.get("code") == JudgeConsants.Judge.STATUS_COMPILE_ERROR.getStatus()) {
            finalJudgeRes.setErrorMessage((String) judgeResult.get("errMsg"));
        }
        finalJudgeRes.setStatus((Integer) judgeResult.get("code"));
        finalJudgeRes.setScore((Integer) judgeResult.getOrDefault("score", null));
        return finalJudgeRes;
    }
}
