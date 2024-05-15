package com.co.backend.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 14:49
 */
@Data
@Accessors(chain = true)
public class SubmitJudgeDTO {
    @NotBlank(message = "题目id不能为空")
    private Long qid;

    @NotBlank(message = "代码语言选择不能为空")
    private String language;

    @NotBlank(message = "提交的代码不能为空")
    private String code;

    @NotBlank(message = "提交的比赛id所属不能为空，若并非比赛提交，请设置为0")
    private Long cid;

    private Long gid;

    private Long tid;
}
