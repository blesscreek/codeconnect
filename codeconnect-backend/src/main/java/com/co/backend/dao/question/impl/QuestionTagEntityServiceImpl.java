package com.co.backend.dao.question.impl;

import com.co.backend.model.po.QuestionTag;
import com.co.backend.mapper.QuestionTagMapper;
import com.co.backend.dao.question.QuestionTagEntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-04-10
 */
@Service
public class QuestionTagEntityServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements QuestionTagEntityService {

    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Override
    public List<String> getTagNamesByQuestionId(Long questionId) {
        List<String> tagNamesByQuestionId = questionTagMapper.getTagNamesByQuestionId(questionId);
        return tagNamesByQuestionId;
    }
}
