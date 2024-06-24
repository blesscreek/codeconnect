package com.co.backend.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-24 13:51
 */
@Data
public class JudgeListReturnDTO {
    /**
     * 判题id
     */
    private Long id;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 语言
     */
    private String language;
    /**
     * 状态
     */
    private String statusName;
    /**
     * 分数
     */
    private Integer score;
}
