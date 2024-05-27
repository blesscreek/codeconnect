package com.co.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-23 20:55
 */
@Data
public class JudgeCaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long caseId;

    private Integer status;

    private String columnName;

    private Integer time;

    private Integer memory;

    private String showTime;

    private String showMemory;

    private Integer score;

    private String color;
}
