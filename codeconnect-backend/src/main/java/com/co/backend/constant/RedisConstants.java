package com.co.backend.constant;

import io.swagger.models.auth.In;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-03-25 22:14
 */
public interface RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:";
    public static final String SUBMIT_NON_CONTEST_LOCK = "submit_non_contest_lock:";

    public static final String HOT_QUESTION = "hot_question";
    public static final String QUESTION_PREFIX = "question:";

    public static final Integer HOT_QUESTION_EXPIRE = 60 * 60 * 24 * 7;

    public static final String illegalJson = "{\"status\":\"error\",\"msg\":\"参数错误\"}";
}
