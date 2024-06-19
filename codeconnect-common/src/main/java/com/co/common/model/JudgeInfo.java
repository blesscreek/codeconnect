package com.co.common.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-16 20:13
 */
@Data
public class JudgeInfo implements Serializable {
    private static final long serialVersionUID = 2345L;
    private List<JudgeCaseInfo> judgeCaseInfoList;

    private Long id;

    private Long qid;

    private String qName;

    private Long uid;

    private String uName;

    private Integer status;

    private String errorMessage;

    private Integer score;

    private Integer time;

    private Integer memory;

    private String showTime;

    private String showMemory;

    private Integer length;

    private String code;

    private String language;

    private LocalDateTime submitTime;

}
