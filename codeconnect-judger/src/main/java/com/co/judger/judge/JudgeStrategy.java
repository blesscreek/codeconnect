package com.co.judger.judge;

import com.co.common.model.JudgeInfo;
import com.co.judger.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description 对各类的编译、各测试点运行评测判题
 * @Date 2024-05-16 21:48
 */
@Component
public class JudgeStrategy {
    @Autowired
    private Compiler compiler;
    public void judge(Question question, JudgeInfo judgeInfo) {
        compiler.compile(judgeInfo.getId(), judgeInfo.getCode(), judgeInfo.getLanguage());
    }
}
