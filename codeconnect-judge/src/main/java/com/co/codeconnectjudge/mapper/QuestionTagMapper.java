package com.co.codeconnectjudge.mapper;

import com.co.codeconnectjudge.model.po.QuestionTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;

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
