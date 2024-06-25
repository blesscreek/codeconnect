package com.co.backend.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description 获取编辑题目列表
 * @Date 2024-06-25 14:48
 */
@Data
public class GetEditQuestionListReturnDTO {
    int questionCnt;

    List<EditQuestionListReturnDTO> editQuestionListReturn;
}
