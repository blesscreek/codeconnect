package com.co.codeconnectjudge.validator;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.constant.QuestionConstants;
import com.co.codeconnectjudge.model.po.Question;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        commonValidator.validateContent(question.getTitle(), "题目名称", 30);
        commonValidator.validateContent(question.getDescription(),"题目描述");
        commonValidator.validateContent(question.getInput(), "输入描述");
        commonValidator.validateContent(question.getOutput(), "输出描述");
        commonValidator.validateContent(question.getExamples(), "题面样例");
        // 匹配双引号中的内容的正则表达式
        String regex = "\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(question.getExamples());
        while (matcher.find()) {
            // 获取双引号中的内容
            String content = matcher.group(1);
            // 判断内容是否为空
            if (content.trim().isEmpty()) {
                throw new StatusFailException("题面样例不能为空");
            }
        }
        defaultValidate(question);
        if (question.getTimeLimit() != null && question.getTimeLimit() <= 0) {
            throw new StatusFailException("题目的时间限制不能小于等于0！");
        }
        if (question.getMemoryLimit() != null && question.getMemoryLimit() <= 0) {
            throw new StatusFailException("题目的内存限制不能小于等于0！");
        }
//        if (question.getStackLimit() <= 0) {
//            throw new StatusFailException("题目的栈限制不能小于等于0！");
//        }
    }

    private void defaultValidate(Question question) throws StatusFailException {
        QuestionConstants.QuestionDifficulty questionDifficulty = QuestionConstants.QuestionDifficulty.getQuestionDifficulty(question.getDifficulty());
        if (questionDifficulty == null) {
            throw new StatusFailException("题目难度必须为EASY(0)或MEDIUM(1)或HARD(2)");
        }
        QuestionConstants.QuestionType questionType = QuestionConstants.QuestionType.getQuestionType(question.getType());
        if (questionType == null) {
            throw new StatusFailException("题目的模式必须为OI(0)或ACM(1)");
        }
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
