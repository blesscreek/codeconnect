package com.co.codeconnectjudge.validator;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.constant.QuestionConstants;
import com.co.codeconnectjudge.model.po.Question;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-07 22:55
 */
@Component
public class QuestionValidator {
    @Autowired
    private CommonValidator commonValidator;

    public void validateQuestion(Question question) throws StatusFailException {
        if (question == null) {
            throw new StatusFailException("题目的配置项不能为空");
        }

        defaultValidate(question);
        if (question.getTimeLimit() <= 0) {
            throw new StatusFailException("题目的时间限制不能小于等于0！");
        }
        if (question.getMemoryLimit() <= 0) {
            throw new StatusFailException("题目的内存限制不能小于等于0！");
        }
        if (question.getStackLimit() <= 0) {
            throw new StatusFailException("题目的栈限制不能小于等于0！");
        }
    }

    private void defaultValidate(Question question) throws StatusFailException {
        QuestionConstants.QuestionAuth questionAuth = QuestionConstants.QuestionAuth.getquestionAuth(question.getAuth());
        if (questionAuth == null) {
            throw new StatusFailException("题目的权限必须为公开(0)或私有(1)");
        }
        QuestionConstants.JudgeMode judgeMode = QuestionConstants.JudgeMode.getJudgeMode(question.getJudgeMode());
        if (judgeMode == null) {
            throw new StatusFailException("题目的判题模式必须为普通判题(default),或特殊判题(spj)");
        }

    }

}
