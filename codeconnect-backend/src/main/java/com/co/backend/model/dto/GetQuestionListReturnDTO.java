package com.co.backend.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-12 12:30
 */
@Data
@ToString
public class GetQuestionListReturnDTO {
    int questionCnt;
    List<QuestionListReturnDTO> questionResturn;
}
