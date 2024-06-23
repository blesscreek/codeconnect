package com.co.backend.model.dto;

import com.co.backend.model.po.Question;
import com.co.backend.model.po.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @Author co
 * @Version 1.0
 * @Description 增加题目传进来的类
 * @Date 2024-04-07 21:01
 */

@Data
@Accessors(chain = true)//对应字段的 setter 方法调用后，会返回当前对象
public class QuestionDTO {
    private Long id;

    private Question question;

    private List<Tag> tags;
}
