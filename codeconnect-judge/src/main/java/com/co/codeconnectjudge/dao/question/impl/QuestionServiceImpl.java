package com.co.codeconnectjudge.dao.question.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.codeconnectjudge.mapper.QuestionMapper;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.model.po.Question;
import com.co.codeconnectjudge.model.po.QuestionCase;
import com.co.codeconnectjudge.dao.question.QuestionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 17:28
 */

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addQuestion(QuestionDTO questionDTO) {
        Question question = questionDTO.getQuestion();
        if (questionDTO.getIsUploadTestCase()) {
            int sumScore = 0;
            String testcaseDir = questionDTO.getUploadTestcaseDir();
            List<QuestionCase> questionCases = questionDTO.getSamples();
            if (questionCases.size() == 0) {
                throw new RuntimeException("The test cases of problem must not be empty!");
            }
            for (QuestionCase questionCase : questionCases) {
                if (questionCase.getScore() != null) {
                    sumScore += questionCase.getScore();
                }
                //如果测试样例的输出没有
                if (StringUtils.isEmpty(questionCase.getOutput())) {

                }
            }
        }

        return false;
    }
}
