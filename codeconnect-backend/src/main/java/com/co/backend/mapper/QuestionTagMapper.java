package com.co.backend.mapper;

import com.co.backend.model.po.QuestionTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author co
 * @since 2024-04-10
 */
public interface QuestionTagMapper extends BaseMapper<QuestionTag> {
    public List<String> getTagNamesByQuestionId(Long questionId);

}