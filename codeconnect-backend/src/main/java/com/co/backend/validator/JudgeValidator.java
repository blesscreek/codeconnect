package com.co.backend.validator;

import com.co.common.exception.StatusFailException;
import com.co.backend.model.dto.SubmitJudgeDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:57
 */
@Component
public class JudgeValidator {
    private final static List<String> languageList = Arrays.asList("C", "Cpp", "Java", "Python", "Go", "C#", "JavaScript", "PHP", "Ruby", "Swift", "Kotlin", "Rust", "Objective-C");

    public void validateSubmissionInfo(SubmitJudgeDTO judgeDTO) throws StatusFailException {
        if (!languageList.contains(judgeDTO.getLanguage())) {
            throw new StatusFailException("提交的代码语言错误！");
        }
        if (judgeDTO.getCode().length() > 65535) {
            throw new StatusFailException("提交的代码是无效的，代码字符长度请不要超过65535！");
        }


    }
}
