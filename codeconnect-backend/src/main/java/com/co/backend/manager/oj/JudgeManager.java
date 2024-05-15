package com.co.backend.manager.oj;

import com.co.backend.constant.JudgeConsants;
import com.co.backend.constant.LimitConstants;
import com.co.backend.constant.RedisConstants;
import com.co.backend.judge.JudgeDispatcher;
import com.co.backend.model.po.Judge;
import com.co.backend.validator.JudgeValidator;
import com.co.backend.common.exception.StatusFailException;
import com.co.backend.common.exception.StatusForbiddenException;
import com.co.backend.model.dto.LoginUser;
import com.co.backend.model.dto.SubmitJudgeDTO;
import com.co.backend.model.po.User;
import com.co.backend.utils.IpUtils;
import com.co.backend.utils.RabbitMQUtil;
import com.co.backend.utils.RedisCache;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private RabbitMQUtil rabbitMQUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private JudgeDispatcher judgeDispatcher;
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
        judge.setUid(user.getId())
                .setUsername(user.getNickName())
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
