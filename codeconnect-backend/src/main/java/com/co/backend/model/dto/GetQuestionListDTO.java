package com.co.backend.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-11 9:28
 */
@Data
@ToString
public class GetQuestionListDTO {
    //题目难度
    private String difficulty;
    //算法标签
    private List<String> tags;
    //关键字（题目名称、题号）
    private String keyword;
}
