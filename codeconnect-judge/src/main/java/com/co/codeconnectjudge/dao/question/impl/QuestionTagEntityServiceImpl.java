package com.co.codeconnectjudge.dao.question.impl;

import com.co.codeconnectjudge.model.po.QuestionTag;
import com.co.codeconnectjudge.mapper.QuestionTagMapper;
import com.co.codeconnectjudge.dao.question.QuestionTagEntityService;
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
