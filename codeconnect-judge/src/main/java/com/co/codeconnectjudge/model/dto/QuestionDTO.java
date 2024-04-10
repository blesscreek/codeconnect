package com.co.codeconnectjudge.model.dto;

import com.co.codeconnectjudge.model.po.Question;
import com.co.codeconnectjudge.model.po.QuestionCase;
import com.co.codeconnectjudge.model.po.Tag;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.parsing.Problem;

import java.util.List;


/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-07 21:01
 */

@Data
@Accessors(chain = true)//对应字段的 setter 方法调用后，会返回当前对象
public class QuestionDTO {

    private Question question;

    private List<QuestionCase> samples;

    private Boolean isUploadTestCase;

    private String uploadTestcaseDir;

    private String judgeMode;

    private List<Tag> tags;
}
