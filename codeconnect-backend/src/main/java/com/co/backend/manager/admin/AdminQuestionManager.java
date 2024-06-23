package com.co.backend.manager.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.backend.constant.QuestionConstants;
import com.co.backend.dao.judge.JudgeCaseEntityService;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.dao.question.QuestionTagEntityService;
import com.co.backend.dao.user.UserEntityService;
import com.co.backend.model.entity.LoginUser;
import com.co.backend.model.po.Judge;
import com.co.backend.model.po.JudgeCase;
import com.co.backend.model.po.PageParams;
import com.co.backend.model.po.Question;
import com.co.common.exception.StatusFailException;
import com.co.backend.model.dto.*;
import com.co.common.exception.StatusForbiddenException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
    @Autowired
    private UserEntityService userEntityService;
    @Autowired
    private JudgeCaseEntityService judgeCaseEntityService;
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
        boolean logined = !(authentication == null || authentication.getName().equals("anonymousUser"));

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
                        .eq(Judge::getUid, uid)
                        .select(Judge::getScore)
                        .orderByDesc(Judge::getScore)
                        .last("LIMIT 1");

                Judge judge = judgeEntityService.getOne(queryWrapper);
                if (judge == null || judge.getScore() == null)
                    //未作答
                    listReturn.setStatus(-1);
                else{
                    listReturn.setStatus(judge.getScore());
                }

            }
            listReturn.setQid(question.getId());
            listReturn.setQuestionNum("Q" + question.getId());
            listReturn.setTitle(question.getTitle());
            listReturn.setTags(questionTagEntityService.getTagNamesByQuestionId(question.getId()));
            listReturn.setSubmitNum(question.getSubmitNum());
            listReturn.setAcceptNum(question.getAcceptNum());
            if (question.getSubmitNum() == 0)
                listReturn.setPassingRate(0);
            else {
                Integer passingRate = (int)((question.getAcceptNum() * (1.0) / question.getSubmitNum()) * 100) ;
                listReturn.setPassingRate(passingRate);
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

    public QuestionReturnDTO showQuestion(Long qid) throws StatusFailException, StatusForbiddenException {
        Question question = questionEntityService.getById(qid);
        if ((question == null) || (question.getIsDelete() == true)) {
            throw new StatusFailException("题目不存在，查询失败");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean logined = !(authentication == null || authentication.getName().equals("anonymousUser"));
        Long uid = -1L;
        if (logined == true) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            uid = loginUser.getUser().getId();
        }
        //将question中的属性放入questionResturnDTO
        QuestionReturnDTO questionReturnDTO = new QuestionReturnDTO();
        BeanUtils.copyProperties(question, questionReturnDTO);
        if (question.getUid() != null) {
            String nickname = userEntityService.getById(question.getUid()).getNickname();
            if (nickname != null) {
                questionReturnDTO.setAuthor(nickname);
            }
        }
        if (logined == true) {
            LambdaQueryWrapper<Judge> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Judge::getQid, question.getId())
                    .eq(Judge::getUid, uid)
                    .select(Judge::getScore)
                    .orderByDesc(Judge::getScore)
                    .last("LIMIT 1");
            Judge judge = judgeEntityService.getOne(queryWrapper);
            if (judge != null) {
                Integer score = judge.getScore();
                if (score != null) questionReturnDTO.setScore(score);
            }
        }
        if (questionReturnDTO.getScore() == null) questionReturnDTO.setScore(-1);

        if (question.getAuth() == 1 && uid != question.getUid()) {
            throw new StatusForbiddenException("该题为私有题目，您无权查看");
        }
        return questionReturnDTO;

    }

    public void deleteQuestion(Long qid) throws StatusFailException {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("id", qid);
        if (questionEntityService.count(questionQueryWrapper) == 0) {
            throw new StatusFailException("该题目不存在，删除失败");
        }
        UpdateWrapper<Question> questionUpdateWrapper = new UpdateWrapper<>();
        questionUpdateWrapper.eq("id", qid)
                .set("is_delete", 1);
        boolean updateQuestion = questionEntityService.update(questionUpdateWrapper);
        if (updateQuestion == false) {
            throw new StatusFailException("删除题目失败");
        }
        //删除该题的判题记录
        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper.eq("qid",qid);
        int count = judgeEntityService.count(judgeQueryWrapper);
        if (count != 0) {
            UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
            judgeUpdateWrapper.eq("qid", qid).set("is_delete", 1);
            boolean updateJudge = judgeEntityService.update(judgeUpdateWrapper);
            if (updateJudge == false) {
                throw new StatusFailException("删除题目相关判题信息失败");
            }
        }
        //删除该题的判题样例记录
        QueryWrapper<JudgeCase> judgeCaseQueryWrapper = new QueryWrapper<>();
        judgeCaseQueryWrapper.eq("qid",qid);
        int count1 = judgeCaseEntityService.count(judgeCaseQueryWrapper);
        if (count1 != 0) {
            UpdateWrapper<JudgeCase> judgeCaseUpdateWrapper = new UpdateWrapper<>();
            judgeCaseUpdateWrapper.eq("qid",qid).set("is_delete",1);
            boolean updateJudgeCase = judgeCaseEntityService.update(judgeCaseUpdateWrapper);
            if (updateJudgeCase == false) {
                throw new StatusFailException("删除题目相关判题样例信息失败");
            }
        }

    }

    public Question getQuestion(Long qid) throws StatusFailException {
        Question question = questionEntityService.getById(qid);
        if ((question == null) || (question.getIsDelete() == true)) {
            throw new StatusFailException("题目不存在，查询失败");
        }
        return question;

    }

    public void updateQuestion(QuestionDTO questionDTO) throws StatusFailException {
        Question question = questionEntityService.getById(questionDTO.getId());
        if (question == null) {
            throw new StatusFailException("题目不存在，更改失败");
        }
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",questionDTO.getQuestion().getTitle());
        question = questionEntityService.getOne(queryWrapper);
        if (question != null) {
            throw new StatusFailException("题目标题重复，请更换");
        }
        boolean updateRes = questionEntityService.updateQuestion(questionDTO);
        if (updateRes == false) {
            throw new StatusFailException("题目更改失败");
        }
    }
}
