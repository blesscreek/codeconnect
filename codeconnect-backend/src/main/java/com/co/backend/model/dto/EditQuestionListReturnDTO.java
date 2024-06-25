package com.co.backend.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description 编辑题目列表所需的题目属性
 * @Date 2024-06-25 14:49
 */
@Data
public class EditQuestionListReturnDTO {

    //题目id
    private Long qid;
    //题号
    private String questionNum;
    //题目名称
    private String title;
    //算法
    private List<String> tags;
    //难度
    private String difficulty;
}
