package com.co.judger.judge;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.dao.JudgeCaseEntityService;
import com.co.judger.dao.JudgeEntityService;
import com.co.judger.model.Judge;
import com.co.judger.model.JudgeCase;
import com.co.judger.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    private JudgeCaseEntityService judgeCaseEntityService;
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
            if (compileRes.get("errMsg") != null) {
                result.put("code", JudgeConsants.Judge.STATUS_COMPILE_ERROR.getStatus());
                result.put("errMsg",compileRes.get("errMsg"));
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

        try {
            List<JSONObject> allCaseResultList = judgeRun.judgeAllCase(question, judgeInfo, compiledPath);
            if (allCaseResultList == null) {
                result.put("code", JudgeConsants.Judge.STATUS_SYSTEM_ERROR.getStatus());
                result.put("errMsg","status system error");
                result.put("time", 0);
                result.put("memory", 0);
            }
            return getJudgeInfo(allCaseResultList, judgeInfo, question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private HashMap<String, Object> getJudgeInfo(List<JSONObject> allCaseResultList, JudgeInfo judgeInfo, Question question) {
        HashMap<String, Object> res = new HashMap<>();
        List<JudgeCase> judgeCases = new LinkedList<>();
        AtomicInteger casePassNum = new AtomicInteger();
        AtomicInteger sumScore = new AtomicInteger();
        allCaseResultList.forEach(jsonObject -> {
            JudgeCase judgeCase = new JudgeCase();
            judgeCase.setJudgeId(Long.valueOf(jsonObject.getInt("judgeId")));
            judgeCase.setUid(Long.valueOf(judgeInfo.getUid()));
            judgeCase.setQid(question.getId());
            judgeCase.setCaseId(Long.valueOf(jsonObject.getInt("caseId")));
            judgeCase.setStatus(jsonObject.getInt("status"));
            judgeCase.setTime(Long.valueOf(jsonObject.getInt("real_time_used_us")));
            judgeCase.setMemory(Long.valueOf(jsonObject.getInt("memory_used")));
            judgeCase.setScore(jsonObject.getInt("score"));
            judgeCases.add(judgeCase);
            if (jsonObject.getInt("status") == 0 || jsonObject.getInt("status") == 1) {
                casePassNum.getAndIncrement();
                sumScore.addAndGet(jsonObject.getInt("score"));
            }
        });
        judgeCaseEntityService.saveBatch(judgeCases);
        res.put("judgeCaseInfoList", judgeCases);
        if (casePassNum.get() == allCaseResultList.size()) {
            res.put("code",JudgeConsants.Judge.STATUS_ACCEPTED.getStatus());
            res.put("score", 100);
        } else {
            res.put("code",JudgeConsants.Judge.STATUS_PARTIAL_ACCEPTED.getStatus());
            res.put("score", sumScore.get());
        }
        return res;
    }
}
