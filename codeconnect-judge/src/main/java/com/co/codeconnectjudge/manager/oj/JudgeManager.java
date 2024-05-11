package com.co.codeconnectjudge.manager.oj;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.common.exception.StatusForbiddenException;
import com.co.codeconnectjudge.config.RabbitmqConfig;
import com.co.codeconnectjudge.constant.JudgeConsants;
import com.co.codeconnectjudge.constant.LimitConstants;
import com.co.codeconnectjudge.constant.RedisConstants;
import com.co.codeconnectjudge.judge.JudgeDispatcher;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.dto.SubmitJudgeDTO;
import com.co.codeconnectjudge.model.po.Judge;
import com.co.codeconnectjudge.model.po.User;
import com.co.codeconnectjudge.utils.IpUtils;
import com.co.codeconnectjudge.utils.RabbitMQUtil;
import com.co.codeconnectjudge.utils.RedisCache;
import com.co.codeconnectjudge.validator.JudgeValidator;
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
