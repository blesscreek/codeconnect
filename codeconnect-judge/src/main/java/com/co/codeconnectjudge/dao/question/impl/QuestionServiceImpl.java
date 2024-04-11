package com.co.codeconnectjudge.dao.question.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.dao.question.QuestionTagService;
import com.co.codeconnectjudge.dao.question.TagService;
import com.co.codeconnectjudge.dao.user.UserService;
import com.co.codeconnectjudge.mapper.QuestionMapper;
import com.co.codeconnectjudge.mapper.QuestionTagMapper;
import com.co.codeconnectjudge.mapper.TagMapper;
import com.co.codeconnectjudge.mapper.UserMapper;
import com.co.codeconnectjudge.model.dto.LoginUser;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.po.*;
import com.co.codeconnectjudge.dao.question.QuestionService;
import com.co.codeconnectjudge.validator.QuestionValidator;
import org.apache.commons.lang.StringUtils;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionValidator questionValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionTagService questionTagService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addQuestion(QuestionDTO questionDTO) throws StatusFailException {
        Question question = questionDTO.getQuestion();
        questionValidator.validateQuestion(question);
        //获取用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        User user = userService.getById(userId);
        String account = user.getAccount();
        //插入question表
        question.setAuthor(account);
        LocalDateTime currentTime = LocalDateTime.now();
        question.setCreateTime(currentTime);
        question.setUpdateTime(currentTime);
        boolean saveQuestion = questionService.save(question);
        if (saveQuestion == false) {
            return false;
        }
        //存入question_tag表
        List<Tag> tags = questionDTO.getTags();

        for (Tag tag : tags) {
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",tag.getName());
            Tag tag1 = tagService.getOne(queryWrapper);
            Long tag1Id = tag1.getId();
            QuestionTag questionTag = new QuestionTag();
            questionTag.setQid(question.getId());
            questionTag.setTid(tag1Id);
            questionTag.setCreateTime(currentTime);
            questionTag.setUpdateTime(currentTime);
            boolean saveQuestionTag = questionTagService.save(questionTag);
            if (saveQuestionTag == false) {
                return false;
            }
        }
        return true;
    }
}
