package com.co.backend.constant;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-05-08 15:44
 */
public interface LimitConstants {
    //非比赛提交时间间隔秒数
    public static final Integer defaultSubmitInterval = 4;

    //布隆过滤器预期插入数量
    static long expectedInsertions = 2000L;

    //误判率
    static double falseProbability = 0.01;
}
