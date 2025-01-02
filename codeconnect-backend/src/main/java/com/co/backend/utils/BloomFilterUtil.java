package com.co.backend.utils;

import com.co.backend.constant.LimitConstants;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class BloomFilterUtil {

    @Resource
    private RedissonClient redissonClient;

    private RBloomFilter<Long> bloomFilter = null;
    @PostConstruct
    public void init() {
        bloomFilter = create("questionList",
                LimitConstants.expectedInsertions,//预期插入数量2000L
                LimitConstants.falseProbability);//误判率0.01
    }

    public boolean isExit(Long questionId) {
        return bloomFilter.contains(questionId);
    }

    public void addQId(Long QuestionId) {
        bloomFilter.add(QuestionId);
    }

    /**
     * 创建布隆过滤器
     *
     * @param filterName         过滤器名称
     * @param expectedInsertions 预测插入数量
     * @param falsePositiveRate  误判率
     */
    public <T> RBloomFilter<T> create(String filterName, long expectedInsertions, double falsePositiveRate) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(filterName);
        bloomFilter.tryInit(expectedInsertions, falsePositiveRate);
        return bloomFilter;
    }
}
