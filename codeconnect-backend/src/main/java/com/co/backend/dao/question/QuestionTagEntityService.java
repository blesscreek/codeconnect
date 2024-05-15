package com.co.backend.dao.question;

import com.co.backend.model.po.QuestionTag;
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
