package com.co.backend.manager.oj;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.backend.dao.question.QuestionEntityService;
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
import com.co.backend.model.dto.LoginUser;
import com.co.backend.model.dto.SubmitJudgeDTO;
import com.co.backend.utils.IpUtils;
import com.co.backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:55
 */
@Component
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
    private QuestionEntityService questionEntityService;
    public Judge submitJudgeQuestion(SubmitJudgeDTO judgeDTO) throws StatusForbiddenException, StatusFailException {
        judgeValidator.validateSubmissionInfo(judgeDTO);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication.getPrincipal() != "anonymousUser") {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            user = loginUser.getUser();
        }
        if (user == null) {
            throw new StatusForbiddenException("对不起，您尚未登录，请登录后重试！");
        }

        boolean isContestSubmission = judgeDTO.getCid() != null && judgeDTO.getCid() != 0;
        boolean isTrainingSubmission = judgeDTO.getTid() != null && judgeDTO.getTid() != 0;

        //非比赛限制提交时间间隔
        if (LimitConstants.defaultSubmitInterval > 0) {
            String lockKey = RedisConstants.SUBMIT_NON_CONTEST_LOCK + user.getId();
            if (!redisCache.isWithinRateLimit(lockKey, LimitConstants.defaultSubmitInterval)) {
                throw new StatusForbiddenException("对不起，您的提交频率过快，请稍后再尝试！");
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        Judge judge = new Judge();
        judge.setQid(judgeDTO.getQid())
                .setUid(user.getId())
                .setNickname(user.getNickname())
                .setSubmitTime(LocalDateTime.now())
                .setStatus(JudgeConsants.Judge.STATUS_NOT_SUBMITTED.getStatus())
                .setShare(false)
                .setLength(judgeDTO.getCode().length())
                .setCode(judgeDTO.getCode())
                .setLanguage(judgeDTO.getLanguage())
                .setCid(judgeDTO.getCid())
                .setGid(judgeDTO.getGid())
                .setIp(IpUtils.getUserIpAddr(request));
        //TODO:比赛和训练提交的初始化
        beforeDispatchInitManager.initCommonSubmission(judgeDTO.getQid(), judgeDTO.getGid(), judge);

        //TODO:远程oj
        //非远程oj
        judgeDispatcher.sendTask(judge);
        return judge;
    }
}
