package com.co.judger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.common.constants.JudgeConsants;
import com.co.common.model.JudgeInfo;
import com.co.judger.dao.JudgeEntityService;
import com.co.judger.dao.QuestionEntityService;
import com.co.judger.judge.JudgeContext;
import com.co.judger.model.Judge;
import com.co.judger.model.Question;
import com.co.judger.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author co
 * @Version 1.0
 * @Description 分普通判断、在线测试判题、远程判题等进入编译状态的数据库更新
 * @Date 2024-05-16 20:58
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    private JudgeEntityService judgeEntityService;
    @Autowired
    private QuestionEntityService questionEntityService;
    @Autowired
    private JudgeContext judgeContext;
    @Override
    public void judgeProcess(JudgeInfo judgeInfo) {
        //设置编译阶段状态
        UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
        judgeUpdateWrapper.set("status", JudgeConsants.Judge.STATUS_COMPILING.getStatus())
                .eq("id",judgeInfo.getId())
                .ne("status",JudgeConsants.Judge.STATUS_CANCELLED.getStatus());
        judgeEntityService.update(judgeUpdateWrapper);
        //获取题目信息,并进入判题
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.select("id","judge_mode","time_limit","memory_limit").eq("id",judgeInfo.getQid());
        Question question = questionEntityService.getOne(questionQueryWrapper);
        Judge finalJudgeRes = judgeContext.judge(question, judgeInfo);

        judgeEntityService.updateById(finalJudgeRes);


    }
}
