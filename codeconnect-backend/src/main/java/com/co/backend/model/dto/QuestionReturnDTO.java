package com.co.backend.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-22 20:56
 */
@Data
@Accessors(chain = true)
public class QuestionReturnDTO {
    private Long id;

    private Integer score;

    private String title;

    private String author;

    private String background;

    private String description;

    private String input;

    private String output;

    private String examples;

    private Integer difficulty;

    private String hint;

    private Long submitNum;

    private Long acceptNum;

    private Long timeLimit;

    private Long memoryLimit;



}
