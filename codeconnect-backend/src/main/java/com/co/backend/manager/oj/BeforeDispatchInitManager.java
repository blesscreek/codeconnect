package com.co.backend.manager.oj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.dao.question.UserRoleEntityService;
import com.co.backend.model.po.Judge;
import com.co.backend.model.po.Question;
import com.co.backend.model.po.UserRole;
import com.co.common.exception.StatusFailException;
import com.co.common.exception.StatusForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 17:38
 */
@Component
public class BeforeDispatchInitManager {
    @Autowired
    private QuestionEntityService questionEntityService;
    @Autowired
    private UserRoleEntityService userRoleEntityService;
    @Autowired
    private JudgeEntityService judgeEntityService;
    public void initCommonSubmission(Long questionId, Long gid, Judge judge) throws StatusFailException {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","auth","is_group","gid","is_delete");
        queryWrapper.eq("id",questionId);
        Question question = questionEntityService.getOne(queryWrapper);

        if (question == null || question.getIsDelete() == true) {
            throw new StatusFailException("错误！当前题目已不存在，不可提交！");
        }

        if (question.getAuth() == 1) {
            throw new StatusFailException("错误！当前题目不可提交！");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id",judge.getUid());
        UserRole userRole = userRoleEntityService.getOne(userRoleQueryWrapper);
//        if (question.getIsGroup()) {
//            if (gid == null) {
//                throw new StatusFailException("提交失败，该题目为团队所属，请你前往指定团队内提交！");
//            }
//            //TODO:判断要么是团队成员，要么是管理员才可提交该团队内部题目
//            judge.setGid(question.getGid());
//        }
        judge.setCpid(0L)
                .setQid(questionId);
        judgeEntityService.save(judge);

    }
}
