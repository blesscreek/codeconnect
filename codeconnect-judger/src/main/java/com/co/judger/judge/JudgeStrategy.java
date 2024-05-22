package com.co.judger.judge;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.dao.JudgeEntityService;
import com.co.judger.model.Judge;
import com.co.judger.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author co
 * @Version 1.0
 * @Description 对各类的编译、各测试点运行评测判题
 * @Date 2024-05-16 21:48
 */
@Component
public class JudgeStrategy {
    @Autowired
    private JudgeEntityService judgeEntityService;
    @Autowired
    private Compiler compiler;
    @Autowired
    private JudgeRun judgeRun;
    public HashMap<String, Object> judge(Question question, JudgeInfo judgeInfo) {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, String> compileRes = null;
        //静态类型语言编译
        if (!(LanguageConstants.Language.JAVASCRIPT.getLanguage().equals(judgeInfo.getLanguage()) ||
            LanguageConstants.Language.PYTHON.getLanguage().equals(judgeInfo.getLanguage())) ) {
            compileRes = compiler.compile(judgeInfo.getId(), judgeInfo.getCode(), judgeInfo.getLanguage());
            if (compileRes.get("err") != null) {
                result.put("code", JudgeConsants.Judge.STATUS_COMPILE_ERROR.getStatus());
                result.put("errMsg",compileRes.get("err"));
                result.put("time", 0);
                result.put("memory", 0);
                return result;
            }

        }
        String compiledPath = compileRes.get("success");
        UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
        judgeUpdateWrapper.set("status", JudgeConsants.Judge.STATUS_JUDGING.getStatus())
                .eq("id", judgeInfo.getId());
        judgeEntityService.update(judgeUpdateWrapper);

        judgeRun.judgeAllCase(question, judgeInfo,compiledPath);
        return null;
    }
}
