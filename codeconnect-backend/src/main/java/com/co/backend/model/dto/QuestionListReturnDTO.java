package com.co.backend.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-11 17:50
 */
@Data
public class QuestionListReturnDTO {
    //提交状态
    private Integer status;
    //题号
    private String questionNum;
    //题目名称
    private String title;
    //算法
    private List<String> tags;

    private Long submitNum;

    private Long acceptNum;
    //通过率
    private String passingRate;
    //难度
    private String difficulty;
}
