package com.co.backend.manager.oj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.backend.dao.judge.JudgeCaseEntityService;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.dao.user.UserEntityService;
import com.co.backend.model.dto.JudgeListReturnDTO;
import com.co.backend.model.po.JudgeCase;
import com.co.backend.model.po.Question;
import com.co.backend.model.po.User;
import com.co.common.constants.JudgeConsants;
import com.co.backend.constant.LimitConstants;
import com.co.backend.constant.RedisConstants;
import com.co.backend.judge.JudgeDispatcher;
import com.co.backend.model.po.Judge;
import com.co.backend.validator.JudgeValidator;
import com.co.common.exception.StatusFailException;
import com.co.common.exception.StatusForbiddenException;
import com.co.backend.model.entity.LoginUser;
import com.co.backend.model.dto.SubmitJudgeDTO;
import com.co.backend.utils.IpUtils;
import com.co.backend.utils.RedisCache;
import com.co.common.exception.StatusNotFoundException;
import com.co.common.model.JudgeCaseInfo;
import com.co.common.model.JudgeInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:55
 */
@Component
@Slf4j
public class JudgeManager {
    @Autowired
    private JudgeValidator judgeValidator;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private BeforeDispatchInitManager beforeDispatchInitManager;
    @Autowired
    private JudgeDispatcher judgeDispatcher;
    @Autowired
    private JudgeEntityService judgeEntityService;
    @Autowired
    private QuestionEntityService questionEntityService;
    @Autowired
    private UserEntityService userEntityService;
    @Autowired
    private JudgeCaseEntityService judgeCaseEntityService;
    public Judge submitJudgeQuestion(SubmitJudgeDTO judgeDTO) throws StatusFailException, StatusNotFoundException {
        judgeValidator.validateSubmissionInfo(judgeDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (!authentication.getPrincipal() .equals("anonymousUser") || authentication.getPrincipal() != null) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            user = loginUser.getUser();
        }
        if (user == null) {
            throw new StatusNotFoundException("对不起，您尚未登录，请登录后重试！");
        }

        boolean isContestSubmission = judgeDTO.getCid() != null && judgeDTO.getCid() != 0;
        boolean isTrainingSubmission = judgeDTO.getTid() != null && judgeDTO.getTid() != 0;

        //非比赛限制提交时间间隔
        if (LimitConstants.defaultSubmitInterval > 0) {
            String lockKey = RedisConstants.SUBMIT_NON_CONTEST_LOCK + user.getId();
            if (!redisCache.isWithinRateLimit(lockKey, LimitConstants.defaultSubmitInterval)) {
                throw new StatusFailException("对不起，您的提交频率过快，请稍后再尝试！");
            }
        }

        Judge judge = new Judge();
        judge.setQid(judgeDTO.getQid())
                .setUid(user.getId())
                .setSubmitTime(LocalDateTime.now())
                .setStatus(JudgeConsants.Judge.STATUS_NOT_SUBMITTED.getStatus())
                .setShare(false)
                .setLength(judgeDTO.getCode().length())
                .setCode(judgeDTO.getCode())
                .setLanguage(judgeDTO.getLanguage())
                .setCid(judgeDTO.getCid())
                .setGid(judgeDTO.getGid())
                .setIp(IpUtils.getServiceIp())
                .setVersion(0)//重新提交时，version+1
                .setIsDelete(false);
        //TODO:比赛和训练提交的初始化
        Long judgeId = beforeDispatchInitManager.initCommonSubmission(judgeDTO.getQid(), judgeDTO.getGid(), judge);

        //插入热门题目统计
        redisCache.redisTemplate.opsForZSet().incrementScore(RedisConstants.HOT_QUESTION,
                judgeDTO.getQid(), 1);
        if (Boolean.FALSE.equals(redisCache.redisTemplate.getExpire(RedisConstants.HOT_QUESTION) > 0)) {
            redisCache.expire(RedisConstants.HOT_QUESTION, RedisConstants.HOT_QUESTION_EXPIRE); // 设为一周的过期时间
        }
        //TODO:远程oj
        //非远程oj
        judgeDispatcher.sendTask(judge);
        return judge;
    }

    public List<JudgeListReturnDTO> getJudgeList(Long qid) throws StatusFailException, StatusNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (!authentication.getPrincipal() .equals("anonymousUser") || authentication.getPrincipal() != null) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            user = loginUser.getUser();
        }
        if (user == null) {
            throw new StatusNotFoundException("对不起，您尚未登录，无法查看提交历史记录");
        }
        Question question = questionEntityService.getById(qid);
        if (question == null || question.getIsDelete() == true) {
            throw new StatusFailException("该题目不存在");
        }
        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper.eq("uid", user.getId())
                .eq("qid", qid)
                .eq("is_delete", false)
                .orderByDesc("submit_time");
        List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
        List<JudgeListReturnDTO> judgeListReturnDTOS = new LinkedList<>();
        for (Judge judge : judgeList) {
            JudgeListReturnDTO judgeListReturnDTO = new JudgeListReturnDTO();
            BeanUtils.copyProperties(judge, judgeListReturnDTO);
            judgeListReturnDTO.setStatusName(JudgeConsants.Judge.getNameFromStatus(judge.getStatus()));
            judgeListReturnDTOS.add(judgeListReturnDTO);
        }
        return judgeListReturnDTOS;


    }

    public Object getJudge(Long jid) throws StatusFailException, StatusNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (!authentication.getPrincipal() .equals("anonymousUser") || authentication.getPrincipal() != null) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            user = loginUser.getUser();
        }
        if (user == null) {
            throw new StatusNotFoundException("对不起，您尚未登录，无法查看提交历史记录");
        }
        Judge judge = judgeEntityService.getById(jid);
        if (judge == null || judge.getIsDelete() == true) {
            throw new StatusFailException("提交记录不存在");
        }
        JudgeInfo judgeInfo = new JudgeInfo();
        BeanUtils.copyProperties(judge, judgeInfo);
        judgeInfo.setQName(questionEntityService.getById(judge.getQid()).getTitle());
        judgeInfo.setUName(userEntityService.getById(judge.getUid()).getNickname());
        judgeInfo.setStatusName(JudgeConsants.Judge.getNameFromStatus(judge.getStatus()));
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
        //设置判题样例信息
        QueryWrapper<JudgeCase> judgeCaseQueryWrapper = new QueryWrapper<>();
        judgeCaseQueryWrapper.eq("judge_id",jid);
        List<JudgeCase> list = judgeCaseEntityService.list(judgeCaseQueryWrapper);
        List<JudgeCaseInfo> judgeCaseInfoList = new LinkedList<>();
        for (JudgeCase judgeCase : list) {
            JudgeCaseInfo judgeCaseInfo = new JudgeCaseInfo();
            BeanUtils.copyProperties(judgeCase, judgeCaseInfo);
            judgeCaseInfo.setColumnName(JudgeConsants.Judge.getColumnNameFromStatus(judgeCase.getStatus()));
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
