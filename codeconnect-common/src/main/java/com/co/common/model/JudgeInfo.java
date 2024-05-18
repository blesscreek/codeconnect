package com.co.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-16 20:13
 */
@Data
public class JudgeInfo implements Serializable {
    private static final long serialVersionUID = 2345L;

    private Long id;

    private Long qid;

    private Integer status;

    private String errorMessage;

    private Integer time;

    private Integer memory;

    private Integer score;

    private Integer length;

    private String code;

    private String language;



}
