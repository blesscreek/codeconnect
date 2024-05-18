package com.co.backend.dao.question.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.common.exception.StatusFailException;
import com.co.backend.dao.question.QuestionTagEntityService;
import com.co.backend.dao.question.TagEntityService;
import com.co.backend.mapper.QuestionMapper;
import com.co.backend.model.dto.LoginUser;
import com.co.backend.model.dto.QuestionDTO;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.validator.QuestionValidator;
import com.co.backend.model.po.Question;
import com.co.backend.model.po.QuestionTag;
import com.co.backend.model.po.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 17:28
 */

@Service
public class QuestionEntityServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionEntityService {

    @Autowired
    private QuestionValidator questionValidator;
    @Autowired
    private TagEntityService tagEntityService;
    @Autowired
    private QuestionTagEntityService questionTagEntityService;
    @Autowired
    private QuestionMapper questionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addQuestion(QuestionDTO questionDTO) throws StatusFailException {
        Question question = questionDTO.getQuestion();
        questionValidator.validateQuestion(question);
        //获取用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        //插入question表
        question.setUid(uid);
        LocalDateTime currentTime = LocalDateTime.now();
        question.setCreateTime(currentTime);
        question.setUpdateTime(currentTime);
        question.setIsDelete(false);
        question.setGroup(false);
        question.setAcceptNum(0L);
        question.setSubmitNum(0L);
        boolean saveQuestion = this.save(question);
        if (saveQuestion == false) {
            return false;
        }
        //存入question_tag表
        List<Tag> tags = questionDTO.getTags();

        for (Tag tag : tags) {
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",tag.getName());
            Tag tag1 = tagEntityService.getOne(queryWrapper);
            Long tag1Id = tag1.getId();
            QuestionTag questionTag = new QuestionTag();
            questionTag.setQid(question.getId());
            questionTag.setTid(tag1Id);
            questionTag.setCreateTime(currentTime);
            questionTag.setUpdateTime(currentTime);
            boolean saveQuestionTag = questionTagEntityService.save(questionTag);
            if (saveQuestionTag == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Question> selectQuestions(String[] tagNames, String titleKeyword, Integer difficulty, Long pageSize, Long offset) {
        List<Question> questions = questionMapper.selectQuestions(tagNames, titleKeyword, difficulty, pageSize, offset);
        return questions;
    }
}
