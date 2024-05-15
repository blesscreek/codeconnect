package com.co.backend.manager.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.backend.constant.QuestionConstants;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.dao.question.QuestionTagEntityService;
import com.co.backend.model.po.Judge;
import com.co.backend.model.po.PageParams;
import com.co.backend.model.po.Question;
import com.co.backend.common.exception.StatusFailException;
import com.co.backend.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-07 22:38
 */

@Component
public class AdminQuestionManager {
    @Autowired
    private QuestionEntityService questionEntityService;
    @Autowired
    private JudgeEntityService judgeEntityService;
    @Autowired
    private QuestionTagEntityService questionTagEntityService;
    public void addQuestion(QuestionDTO questionDTO) throws StatusFailException {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",questionDTO.getQuestion().getTitle());
        Question question = questionEntityService.getOne(queryWrapper);
        if (question != null) {
            throw new StatusFailException("题目标题重复，请更换");
        }
        boolean addRes = questionEntityService.addQuestion(questionDTO);
        if (addRes == false) {
            throw new StatusFailException("题目添加失败");
        }
    }

    public GetQuestionListReturnDTO getQuestionList(PageParams pageParams, GetQuestionListDTO getQuestionListDTO) throws StatusFailException {
        Long offset = (pageParams.getPageNo() - 1) * pageParams.getPageSize();
        String[] tags;
        if(getQuestionListDTO.getTags() == null || getQuestionListDTO.getTags().size() == 0)
            tags = null;
        else {
            tags = new String[getQuestionListDTO.getTags().size()];
            // 将对象数组中的每个元素转换为字符串，并放入新创建的字符串数组中
            for (int i = 0; i < getQuestionListDTO.getTags().size(); i++) {
                tags[i] = String.valueOf(getQuestionListDTO.getTags().get(i));
            }
        }
        Integer difficulty = QuestionConstants.QuestionDifficulty.getQuestionDifficultyByString(getQuestionListDTO.getDifficulty());
        List<Question> questions = questionEntityService.selectQuestions(tags, getQuestionListDTO.getKeyword(),
                difficulty, pageParams.getPageSize(), offset);
        if (questions == null) {
            throw new StatusFailException("查询题目列表失败");
        }
        //获取用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean logined = false;
        if (authentication.getPrincipal() != "anonymousUser") {
            logined = true;
        }
        Long uid = -1L;
        if (logined == true) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            uid = loginUser.getUser().getId();
        }
        List<QuestionListReturnDTO> questionListReturnDTOS = new ArrayList<>();
        for (Question question : questions) {
            QuestionListReturnDTO listReturn = new QuestionListReturnDTO();
            if (logined == false) {
                //未登录
                listReturn.setStatus(-1);
            } else {
                LambdaQueryWrapper<Judge> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Judge::getQid, question.getId())
                        .eq(Judge::getUid, uid);
                Judge judge = judgeEntityService.getOne(queryWrapper);
                if (judge == null)
                    //未作答
                    listReturn.setStatus(-1);
                else
                    listReturn.setStatus(judge.getScore());
            }
            listReturn.setQuestionNum("Q" + question.getId());
            listReturn.setTitle(question.getTitle());
            listReturn.setTags(questionTagEntityService.getTagNamesByQuestionId(question.getId()));
            listReturn.setSubmitNum(question.getSubmitNum());
            listReturn.setAcceptNum(question.getAcceptNum());
            if (question.getSubmitNum() == 0)
                listReturn.setPassingRate("0");
            else {
                DecimalFormat df = new DecimalFormat("#.##");
                double passingRate = question.getAcceptNum() * (1.0) / question.getSubmitNum();
                listReturn.setPassingRate(df.format(passingRate));
            }
            String difficultyName = QuestionConstants.QuestionDifficulty.
                    getQuestionDifficulty(question.getDifficulty()).name();
            listReturn.setDifficulty(difficultyName);
            questionListReturnDTOS.add(listReturn);
        }
        GetQuestionListReturnDTO getQuestionListReturnDTO = new GetQuestionListReturnDTO();
        getQuestionListReturnDTO.setQuestionCnt(questions.size());
        getQuestionListReturnDTO.setQuestionResturn(questionListReturnDTOS);
        return getQuestionListReturnDTO;

    }
}
