package com.co.codeconnectjudge.dao.question;

import com.co.codeconnectjudge.model.po.QuestionTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author co
 * @since 2024-04-10
 */
public interface QuestionTagEntityService extends IService<QuestionTag> {
    public List<String> getTagNamesByQuestionId(Long questionId);
}
