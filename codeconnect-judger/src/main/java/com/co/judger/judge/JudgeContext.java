package com.co.judger.judge;

import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeCaseInfo;
import com.co.common.model.JudgeInfo;
import com.co.judger.dao.JudgeEntityService;
import com.co.judger.model.Judge;
import com.co.judger.model.JudgeCase;
import com.co.judger.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description 各类判题的题目限制设置和判题结果的设置返回
 * @Date 2024-05-16 21:26
 */
@Component
@Slf4j
public class JudgeContext {
    @Autowired
    private JudgeStrategy judgeStrategy;
    @Autowired
    private JudgeEntityService judgeEntityService;

    public JudgeInfo judge(Question question, JudgeInfo judgeInfo) {
        //c和c++为一倍时间和空间，其它语言为2倍时间和空间
        if (!LanguageConstants.Language.C.getLanguage().equals(judgeInfo.getLanguage())
                && !LanguageConstants.Language.CPP.getLanguage().equals(judgeInfo.getLanguage())) {
            question.setTimeLimit(question.getTimeLimit() * 2);
            question.setMemoryLimit(question.getMemoryLimit() * 2);
        }
        //进入判题
        HashMap<String, Object> judgeResult = judgeStrategy.judge(question, judgeInfo);
        saveJudge(judgeResult, judgeInfo.getId());
        List<JudgeCase> judgeCases = (List<JudgeCase>) judgeResult.get("judgeCases");
//        if (judgeCases == null) {
//            log.error("judgeCases is null");
//            return null;
//        }
        JudgeInfo judgeInfoRes = setResInfo(judgeInfo, judgeCases);
        return judgeInfoRes;
    }
    private void saveJudge(HashMap<String, Object> judgeResult, Long judgeId) {
        Judge finalJudgeRes = new Judge();
        finalJudgeRes.setId(judgeId);
        if (judgeResult.get("code") == JudgeConsants.Judge.STATUS_COMPILE_ERROR.getStatus()) {
            finalJudgeRes.setErrorMessage((String) judgeResult.get("errMsg"));
        }
        finalJudgeRes.setTime((Integer) judgeResult.get("time"));
        finalJudgeRes.setMemory((Integer) judgeResult.get("memory"));
        finalJudgeRes.setStatus((Integer) judgeResult.get("code"));
        finalJudgeRes.setScore((Integer) judgeResult.getOrDefault("score", null));
        judgeEntityService.updateById(finalJudgeRes);
    }
    private JudgeInfo setResInfo(JudgeInfo judgeInfo, List<JudgeCase> judgeCases) {
        Judge judge = judgeEntityService.getById(judgeInfo.getId());
        judgeInfo.setStatus(judge.getStatus());
        judgeInfo.setStatusName(JudgeConsants.Judge.getNameFromStatus(judge.getStatus()));
        judgeInfo.setErrorMessage(judge.getErrorMessage());
        judgeInfo.setScore(judge.getScore());
        judgeInfo.setTime(judge.getTime());
        judgeInfo.setMemory(judge.getMemory());
        judgeInfo.setCreateTime(judge.getCreateTime());
        if (judgeInfo.getTime() > 1000) {
            judgeInfo.setShowTime(String.format("%.2f", judgeInfo.getTime() / 1000.0) + "s");
        } else {
            judgeInfo.setShowTime(judgeInfo.getTime() + "ms");
        }
        if (judgeInfo.getMemory() > 1024) {
            judgeInfo.setShowMemory(String.format("%.2f", judgeInfo.getMemory() / 1024.0) + "MB");
        } else {
            judgeInfo.setShowMemory(judgeInfo.getMemory() + "KB");
        }
        if (judgeCases == null) return judgeInfo;
        List<JudgeCaseInfo> judgeCaseInfoList = new LinkedList<>();
        for (JudgeCase judgeCase : judgeCases) {
            JudgeCaseInfo judgeCaseInfo = new JudgeCaseInfo();
            BeanUtils.copyProperties(judgeCase, judgeCaseInfo);
            if (judgeCaseInfo.getStatus() == JudgeConsants.Judge.STATUS_ACCEPTED.getStatus() ||
                    judgeCaseInfo.getStatus() == JudgeConsants.Judge.PATTERN_ERROR.getStatus()){
                judgeCaseInfo.setColor(JudgeConsants.JudgeColor.AC.getColor());
            } else if (judgeCaseInfo.getStatus() == JudgeConsants.Judge.STATUS_WRONG_ANSWER.getStatus()) {
                judgeCaseInfo.setColor(JudgeConsants.JudgeColor.WA.getColor());
            } else {
                judgeCaseInfo.setColor(JudgeConsants.JudgeColor.EXCEEDLIMIT.getColor());
            }
            judgeCaseInfo.setColumnName(JudgeConsants.Judge.getColumnNameFromStatus(judgeCaseInfo.getStatus()));
            if (judgeCaseInfo.getTime() >= 1000) {
                judgeCaseInfo.setShowTime(String.format("%.2f", judgeCaseInfo.getTime() / 1000.0) + "s");
            } else {
                judgeCaseInfo.setShowTime(judgeCaseInfo.getTime() + "ms");
            }
            if (judgeCaseInfo.getMemory() >= 1024) {
                judgeCaseInfo.setShowMemory(String.format("%.2f", judgeCaseInfo.getMemory() / 1024.0) + "MB");
            } else {
                judgeCaseInfo.setShowMemory(judgeCaseInfo.getMemory() + "KB");
            }
            judgeCaseInfoList.add(judgeCaseInfo);
        }

        judgeInfo.setJudgeCaseInfoList(judgeCaseInfoList);
        return judgeInfo;

    }
}
